package com.geektechkb.feature_main.data.remote.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_USERS_COLLECTION_PATH
import com.geektechkb.feature_main.domain.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.io.IOException

class UsersPagingSource(
    private val firebaseFirestore: FirebaseFirestore,
) : PagingSource<QuerySnapshot, User>() {
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, User> {
        return try {
            val currentPage =
                params.key ?: firebaseFirestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH)
                    .limit(2).get().await()
            val lastVisibleMessage = currentPage.documents[currentPage.size() - 1]
            val nextPage =
                firebaseFirestore.collection(FIREBASE_FIRESTORE_USERS_COLLECTION_PATH).limit(1)
                    .startAfter(lastVisibleMessage).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(User::class.java),
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (http: HttpException) {
            LoadResult.Error(http)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }

    }

    override fun getRefreshKey(state: PagingState<QuerySnapshot, User>): QuerySnapshot? {
        return null
    }
}