package com.geektechkb.feature_main.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.typealiases.NotAnActualPagingData
import com.geektechkb.feature_main.data.remote.pagingsources.UsersPagingSource
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : BaseRepository(), UsersRepository {
    private val usersRef = firestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
    private val sortedUsers = usersRef.orderBy("name", Query.Direction.ASCENDING)

     fun fetchPagedUsersr() : Flow<PagingData<User>>{
         return Pager(
             config = PagingConfig(
                 pageSize = 1,
                 prefetchDistance = 1,
                 enablePlaceholders = true,
                 initialLoadSize = 2,
                 maxSize = Int.MAX_VALUE,
                 jumpThreshold = Int.MIN_VALUE
             ),
             pagingSourceFactory = { UsersPagingSource(sortedUsers) }
         ).flow
     }
    fun fetchIng() = doRequest{
        fetchList<User>(usersRef)
    }
    override fun fetchPagedUsers(): NotAnActualPagingData {
        TODO("Not yet implemented")
    }

    override suspend fun fetchUser(phoneNumber: String) = doRequest {
        return@doRequest User(
            getDocument(usersRef, phoneNumber).get("name") as String,
            getDocument(usersRef, phoneNumber).get("surname") as String,
            getDocument(usersRef, phoneNumber).get("id") as String,
            getDocument(usersRef, phoneNumber).get("profileImage") as String
        )

    }


}