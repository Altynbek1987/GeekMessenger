package com.geektechkb.core.base

import com.geektechkb.common.either.Either
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await


abstract class BaseRepository {
    protected fun <T> doRequest(
        gatherIfSucceed: ((T) -> Unit)? = null,
        request: suspend () -> T,
    ) = flow<Either<String, T>> {
        request().also { data ->
            gatherIfSucceed?.invoke(data)
            emit(Either.Right(value = data))
        }

    }.flowOn(Dispatchers.IO).catch { exception ->
        emit(Either.Left(exception.localizedMessage ?: "An error occurred"))
    }

    suspend inline fun <reified T> fetchList(collection: CollectionReference) =
        collection.get().await().documents.mapNotNull { doc ->
            doc.toObject(T::class.java)
        }

    suspend fun addDocument(
        collection: CollectionReference,
        hashMap: HashMap<String, Any>,
        title: String? = null,
    ): Boolean {
        return try {
            if (title != null) {
                collection
                    .document(title)
                    .set(hashMap)
                    .await()
            } else {
                collection
                    .document()
                    .set(hashMap)
                    .await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getDocument(collection: CollectionReference, id: String): DocumentSnapshot =
        collection
            .document(id)
            .get()
            .await()
}