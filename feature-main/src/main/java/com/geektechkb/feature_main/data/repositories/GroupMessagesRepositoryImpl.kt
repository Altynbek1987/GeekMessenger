package com.geektechkb.feature_main.data.repositories

import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.Attribute
import com.algolia.search.model.IndexName
import com.algolia.search.model.filter.Filter
import com.algolia.search.model.search.Query
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GroupMessagesRepositoryImpl @Inject constructor(
	val firestore: FirebaseFirestore
) : BaseRepository(), GroupMessagesRepository {

	private val groupMessageRef = "groupMessages"
	private val groupRef = firestore.collection("groups")
	private val groupMessageMap = hashMapOf<String, Any?>()

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


	override suspend fun sendMessageToGroup(
		groupName: String,
		senderPhoneNumber: String,
		receiversPhoneNumbers: List<String>,
		message: String,
		timeMessageWasSent: String,
		messageId: String,

	) {
		groupMessageMap["messageId"] = messageId
		groupMessageMap["message"] = message
		groupMessageMap["senderPhoneNumber"] = senderPhoneNumber
		groupMessageMap["receiversPhoneNumbers"] = receiversPhoneNumbers
		groupMessageMap["timeMessageWasSent"] = timeMessageWasSent
		addChildDocument(
			groupRef,
			groupMessageRef,
			groupMessageMap,
			groupName
		)
	}

	override fun fetchGroupInfo(groupName: String) = doRequest {
		getDocument(groupRef, groupName).toObject(Group::class.java) as Group
	}

	override fun fetchGroups() = doRequest {
		fetchList<Group>(groupRef)
	}


	override fun fetchGroupMessages(groupName: String): Flow<List<GroupMessage?>> =
		groupRef.document(groupName)
			.collection(groupMessageRef)
			.orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT)
			.limitToLast(1000)
			.snapshotFlow()
			.map { list ->
				list.map { document ->
					document.toObject(GroupMessage::class.java)
				}
			}


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
			Filter.Facet(Attribute("phoneNumber"), "")
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
		Query("userNumber")
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