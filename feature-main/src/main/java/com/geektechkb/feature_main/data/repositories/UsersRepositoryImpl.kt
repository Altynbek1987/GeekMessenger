package com.geektechkb.feature_main.data.repositories

import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_USER_LAST_SEEN_TIME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_NAME_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_PROFILE_IMAGE_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_USER_SURNAME_KEY
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.local.preferences.UserPreferencesHelper
import com.geektechkb.feature_main.data.remote.pagingsources.UsersPagingSource
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    private val usersPreferencesHelper: UserPreferencesHelper
) : BaseRepository(), UsersRepository {
    private val usersRef = firestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
    private val sortedUsers = usersRef.orderBy("name", Query.Direction.ASCENDING)

    override fun fetchPagedUsers() =
        doPagingRequest(UsersPagingSource(sortedUsers))


    override suspend fun fetchUser(phoneNumber: String) = doRequest {
        return@doRequest User(
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_NAME_KEY) as String,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_SURNAME_KEY) as String,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_PHONE_NUMBER_KEY) as String,
            getDocument(usersRef, phoneNumber).get(FIREBASE_USER_PROFILE_IMAGE_KEY) as String
        )

    }

    override suspend fun updateUserStatus(status: String) {
        updateASingleFieldInDocument(
            usersRef, usersPreferencesHelper.currentUserPhoneNumber,
            FIREBASE_USER_LAST_SEEN_TIME_KEY, status
        )
    }


}