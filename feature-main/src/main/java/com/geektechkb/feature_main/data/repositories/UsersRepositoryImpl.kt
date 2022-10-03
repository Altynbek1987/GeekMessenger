package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import androidx.paging.PagingConfig
import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.algolia.search.model.APIKey
import com.algolia.search.model.ApplicationID
import com.algolia.search.model.IndexName
import com.geektechkb.common.constants.Constants.FIREBASE_CLOUD_STORAGE_PROFILE_IMAGES_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_SEEN_TIME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_HIDDENNESS
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PROFILE_IMAGE_KEY
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.feature_main.data.remote.services.MessengerNotificationsService
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.tasks.await
import java.io.File
import javax.inject.Inject


class UsersRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,
) : BaseRepository(), UsersRepository {
    private val usersRef =
        firestore.collection(FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH)
    private val cloudStorageRef = cloudStorage.reference

    override fun fetchUser(phoneNumber: String) = doRequest {
        return@doRequest User(
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_NAME_KEY) as String?,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_LAST_NAME_KEY) as String?,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_PHONE_NUMBER_KEY) as String?,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_PROFILE_IMAGE_KEY) as String?,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_LAST_SEEN_TIME_KEY) as String?
        )

    }

    override fun updateUserStatus(status: String) {
        firebaseAuth.currentUser?.let {
            updateASingleFieldInDocument(
                usersRef, firebaseAuth.currentUser?.phoneNumber.toString(),
                FIREBASE_USER_LAST_SEEN_TIME_KEY, status
            )
        }

    }

    override suspend fun updateUserProfileImage(url: String): String {
        val file = Uri.fromFile(File(url))
        return file.let {
            cloudStorageRef.child("profileImages/${generateRandomId()}")
            .putFile(file)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString() }
    }

    override fun updateUserName(name: String) {
        firebaseAuth.currentUser?.let {
            updateASingleFieldInDocument(
                usersRef,
                it.phoneNumber.toString(),
                FIREBASE_USER_NAME_KEY,
                name
            )
        }
    }

    override fun updateUserLastName(lastName: String) {
        firebaseAuth.currentUser?.let {
            updateASingleFieldInDocument(
                usersRef,
                it.phoneNumber.toString(),
                FIREBASE_USER_LAST_NAME_KEY,
                lastName
            )
        }
    }

    override suspend fun updateUserProfileImageInFireStore(url: String) {
        firebaseAuth.currentUser?.let {
            updateASingleFieldInDocument(
                usersRef,
                it.phoneNumber.toString(),
                FIREBASE_USER_PROFILE_IMAGE_KEY,
                url
            )
        }
    }
    override fun updateUserNumberHiddenness(isUserPhoneNumberHidden: Boolean) {
        firebaseAuth.currentUser?.let {
            updateASingleFieldInDocument(
                usersRef, it.phoneNumber.toString(),
                FIREBASE_USER_PHONE_NUMBER_HIDDENNESS, isUserPhoneNumberHidden
            )
        }
    }

    override fun subscribeToNotificationTopic(vararg topics: String) {
        MessengerNotificationsService.subscribeToTopic(*topics)

    }

    override fun unsubscribeFromNotificationTopic(vararg topics: String) {
        MessengerNotificationsService.unsubscribeFromTopic(*topics)
    }

    override fun createHitsSearcher(
        applicationId: String,
        apiKey: String,
        indexName: String
    ) = HitsSearcher(
        ApplicationID(applicationId),
        APIKey(apiKey),
        IndexName(indexName),
    )

    override fun createPaginator(notAnActualHitsSearcher: NotAnActualHitsSearcher) = Paginator(
        notAnActualHitsSearcher as HitsSearcher,
        PagingConfig(
            pageSize = 2,
            prefetchDistance = 1,
            enablePlaceholders = false,
            initialLoadSize = 2,
            maxSize = Int.MAX_VALUE,
            jumpThreshold = Int.MIN_VALUE
        ), transformer = { hit -> hit.deserialize(User.serializer()) })

    override fun getCurrentUserPhoneNumber() = firebaseAuth.currentUser?.phoneNumber
}