package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_SEEN_TIME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_HIDDENNESS
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PROFILE_IMAGE_KEY
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.feature_main.data.local.db.daos.UserDao
import com.geektechkb.feature_main.data.remote.pagingsources.UsersPagingSource
import com.geektechkb.feature_main.data.remote.services.MessengerNotificationsService
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.models.UserDb
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.io.File
import java.net.URI
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,
) : BaseRepository(), UsersRepository {
    private val usersRef =
        firestore.collection(FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH)
    private val usersExcludingTheCurrent = usersRef
        .whereNotEqualTo(
            FIREBASE_USER_PHONE_NUMBER_KEY,
            firebaseAuth.currentUser?.phoneNumber
        ).orderBy(FIREBASE_USER_PHONE_NUMBER_KEY)
    private val cloudStorageRef = cloudStorage.reference


    override fun fetchPagedUsers() =
        doPagingRequest(
            UsersPagingSource(
                usersExcludingTheCurrent
            )
        )


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
        return cloudStorageRef.child("profileImages/${generateRandomId()}")
            .putFile(file)
            .await()
            .storage
            .downloadUrl
            .await()
            .toString()
    }


    override fun getUser(): Flow<List<UserDb>> {
        TODO("Not yet implemented")
    }

    override fun insertAllUser(user: UserDb) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: UserDb) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(user: UserDb) {

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

}


