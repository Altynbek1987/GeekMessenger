package com.geektechkb.feature_main.data.remote.pagingsources

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.geektechkb.feature_main.domain.models.Message
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import java.io.IOException

class MessagePagingSource(
    private val query: Query,
) : PagingSource<QuerySnapshot, Message>() {
    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Message> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleMessage = currentPage
                .documents[currentPage.size() - 1]
            val nextPage = query.limit(1).startAfter(lastVisibleMessage).get().await()
            LoadResult.Page(
                data = currentPage.toObjects(Message::class.java),
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

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Message>): QuerySnapshot? {
        return null
    }
}