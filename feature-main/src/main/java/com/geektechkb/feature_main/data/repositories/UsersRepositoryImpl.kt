package com.geektechkb.feature_main.data.repositories

import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.remote.pagingsources.UsersPagingSource
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : BaseRepository(), UsersRepository {
    private val usersRef = firestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
    override fun fetchPagedUsers() = doPagingRequest(UsersPagingSource(firestore))
    override suspend fun fetchUser(phoneNumber: String) = doRequest {
        return@doRequest User(
            getDocument(usersRef, phoneNumber).get("name") as String?,
            getDocument(usersRef, phoneNumber).get("surname") as String?,
            getDocument(usersRef, phoneNumber).get("id") as String?,
            getDocument(usersRef, phoneNumber).get("profileImage") as String?
        )

    }


}