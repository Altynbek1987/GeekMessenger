package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.geektechkb.common.constants.Constants
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.core.extensions.snapshotFlow
import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.feature_main.domain.models.Group
import com.geektechkb.feature_main.domain.models.GroupMessage
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.GroupMessagesRepository
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupMessagesRepositoryImpl @Inject constructor(
    val firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,
) : BaseRepository(), GroupMessagesRepository {

    private val groupMessageRef = "groupMessages"
    private val groupMediaRef = "groupMedia"
    private val groupRef = firestore.collection("groups")
    private val groupMessageMap = hashMapOf<String, Any?>()
    private val cloudStorageRef = cloudStorage.reference


    override suspend fun addUserToGroup(
        groupName: String,
        userList: List<User>,
        groupPhoto: String,
        userCount: Int,
        userNumber: String
    ) {
        addChildGroupDocument(
            groupRef,
            hashMapOf(
                "groupId" to generateRandomId(),
                "groupName" to groupName,
                "groupMembers" to userList,
                "groupPhoto" to groupPhoto,
                "userCount" to userCount,
                "userNumber" to userNumber
            ),
            groupName,
        )
    }

//    override suspend fun senMediaToGroup(
//        media: String?,
//        mediaType: String?,
//        mediaResource: String
//    ) {
//        groupMessageMap["media"] = media to uploadUncompressedMediaToCloudStorage(
//            cloudStorageRef,
//            Uri.parse(media),
//            Constants.FIREBASE_CLOUD_STORAGE_MESSAGE_IMAGES_PATH, generateRandomId()
//        )
//        groupMessageMap["mediaType"] = mediaType
//        groupMessageMap["mediaResource"] = mediaResource
//        addChildDocument(
//            groupRef, groupMediaRef, groupMessageMap, media,
//        )
//    }


    override suspend fun sendMessageToGroup(
        groupName: String,
        senderPhoneNumber: String,
        receiversPhoneNumbers: List<String>,
        message: String,
        media: String?,
        mediaType: String,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        groupMessageMap["messageId"] = messageId
        groupMessageMap["senderPhoneNumber"] = senderPhoneNumber
        groupMessageMap["receiversPhoneNumbers"] = receiversPhoneNumbers
        groupMessageMap["message"] = message
        if (media != null) {
            groupMessageMap["media"] = uploadUncompressedMediaToCloudStorage(
                cloudStorageRef,
                Uri.parse(media),
                Constants.FIREBASE_CLOUD_STORAGE_GROUP_MESSAGE_IMAGES_PATH, generateRandomId()
            )
            groupMessageMap["mediaType"] = mediaType
        } else {
            groupMessageMap["media"] = null
            groupMessageMap["mediaType"] = null
        }
        groupMessageMap["timeMessageWasSent"] = timeMessageWasSent
        addChildDocument(
            groupRef, groupMessageRef, groupMessageMap, groupName,
        )
    }


//    override fun fetchMediaToGroup(media: String): Flow<List<GroupMessage?>> =
//        groupRef.document(media)
//            .collection(groupMediaRef)
//            .orderBy("media")
//            .limitToLast(1000)
//            .snapshotFlow()
//            .map { list ->
//                list.map { document ->
//                    document.toObject(GroupMessage::class.java)
//                }
//            }


    override fun fetchGroupInfo(groupName: String) = doRequest {
        getDocumentGroup(groupRef, groupName).toObject(Group::class.java) as Group
    }

    override fun fetchGroups() = doRequest {
        fetchList<Group>(groupRef)
    }

    override fun fetchGroupMessages(groupName: String) =
        groupRef.document(groupName)
            .collection(groupMessageRef)
            .orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT)
            .limitToLast(10000)
            .snapshotFlow()
            .map { list ->
                list.map { document ->
                    document.toObject(GroupMessage::class.java)
                }
            }
//    override fun fetchGroupMessages(groupName: String, photo: String): Flow<List<GroupMessage?>> =
//        groupRef.document(groupName)
//            .collection(groupMessageRef)
//            .orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT)
//            .limitToLast(1000)
//            .snapshotFlow()
//            .map { list ->
//                list.map { document ->
//                    document.toObject(GroupMessage::class.java)
//                }
//            }

    override fun createGroupPaginator(notAnActualHitsSearcher: NotAnActualHitsSearcher) = Paginator(
        notAnActualHitsSearcher as HitsSearcher,
        PagingConfig(
            pageSize = 2,
            prefetchDistance = 1,
            enablePlaceholders = false,
            initialLoadSize = 2,
            maxSize = Int.MAX_VALUE,
            jumpThreshold = Int.MIN_VALUE
        ), transformer = { hit ->
            hit.deserialize(Group.serializer())
        })


    override fun createHitsSearcher(
        applicationId: String,
        apiKey: String,
        indexName: String,
        query: String
    ) = HitsSearcher(
        ApplicationID(applicationId),
        APIKey(apiKey),
        IndexName(indexName),
    )

    suspend fun addChildGroupDocument(
        mainCollection: CollectionReference,
        mainHashMap: HashMap<String, Any?>,
        id: String? = null,
    ): Boolean {
        return try {
            if (id != null) {
                mainCollection
                    .document(id)
                    .set(mainHashMap)
                    .await()
            } else {
                mainCollection
                    .document()
                    .set(mainHashMap)
                    .await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}
