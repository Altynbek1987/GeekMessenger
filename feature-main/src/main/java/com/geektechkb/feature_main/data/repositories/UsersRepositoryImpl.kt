package com.geektechkb.feature_main.data.repositories

import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_SEEN_TIME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PROFILE_IMAGE_KEY
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.local.db.daos.UserDao
import com.geektechkb.feature_main.data.remote.pagingsources.UsersPagingSource
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val firebaseAuth: FirebaseAuth,
    firestore: FirebaseFirestore,
) : BaseRepository(), UsersRepository {
    private val usersRef =
        firestore.collection(FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH)
    private val usersExcludingTheCurrent = usersRef
        .whereNotEqualTo(
            FIREBASE_USER_PHONE_NUMBER_KEY,
            firebaseAuth.currentUser?.phoneNumber
        ).orderBy(FIREBASE_USER_PHONE_NUMBER_KEY)


    override fun fetchPagedUsers() =
        doPagingRequest(
            UsersPagingSource(
                usersExcludingTheCurrent
            )
        )


    override suspend fun fetchUser(phoneNumber: String) = doRequest {
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

    override fun getUser(): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun insertAllUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun updateUser(user: User) {
        TODO("Not yet implemented")
    }

    override fun deleteUser(user: User) {
        TODO("Not yet implemented")
    }

}


