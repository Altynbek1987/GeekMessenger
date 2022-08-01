package com.geektechkb.core.base

import android.net.Uri
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import com.geektechkb.common.either.Either
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.storage.StorageReference
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


    protected fun <Key : Any, Model : Any> doPagingRequest(
        pagingSource: PagingSource<Key, Model>,
    ) =
        Pager(
            PagingConfig(
                pageSize = 1,
                prefetchDistance = 1,
                enablePlaceholders = true,
                initialLoadSize = 2,
                maxSize = Int.MAX_VALUE,
                jumpThreshold = Int.MIN_VALUE

            ),
            pagingSourceFactory = {
                pagingSource
            }
        ).flow


    suspend inline fun <reified T> fetchList(collection: CollectionReference) =
        collection.get().await().documents.mapNotNull { doc ->
            doc.toObject(T::class.java)
        }

    suspend fun addDocument(
        collection: CollectionReference,
        hashMap: HashMap<String, Any?>,
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

    suspend fun addChildDocument(
        mainCollection: CollectionReference,
        childCollection: String,
        hashMap: HashMap<String, Any>,
        id: String? = null,
    ): Boolean {
        return try {
            if (id != null) {
                mainCollection
                    .document(id)
                    .collection(childCollection)
                    .document()
                    .set(hashMap)
                    .await()
            } else {
                mainCollection
                    .document()
                    .collection(childCollection)
                    .document()
                    .set(hashMap)
                    .await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getChildDocument(
        mainCollection: CollectionReference,
        childCollection: String,
        id: String? = null,
    ): Boolean {
        return try {
            if (id != null) {
                mainCollection
                    .document(id)
                    .collection(childCollection)
                    .document()
                    .get()
                    .await()
            } else {
                mainCollection
                    .document()
                    .collection(childCollection)
                    .document()
                    .get()
                    .await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }


    suspend fun uploadUncompressedImageToCloudStorage(
        storageRef: StorageReference,
        file: Uri?,
        folderPath: String,
        id: String,
        doOnComplete: () -> Unit
    ) =
        file?.let {
            storageRef
                .child("$folderPath/$id")
                .putFile(it)
                .await()
                .storage
                .downloadUrl
                .addOnCompleteListener {
                    doOnComplete()
                }
                .await()
                .toString()
        }


    suspend fun uploadCompressedImageToCloudStorage(
        storageRef: StorageReference,
        file: ByteArray?,
        folderPath: String,
        id: String,
    ) =
        file?.let {
            storageRef
                .child("$folderPath/$id")
                .putBytes(it)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

        }


    suspend fun uploadVoiceMessageToCloudStorage(
        storageRef: StorageReference,
        file: Uri?,
        folderPath: String,
        id: String,
    ) =
        file?.let {
            storageRef
                .child("$folderPath/$id")
                .child(id)
                .putFile(it)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()

        }


    fun updateASingleFieldInDocument(
        collection: CollectionReference,
        documentPath: String,
        fieldToUpdate: String,
        valueToReplaceTheOldOne: Any,
    ) {
        collection.document(documentPath).update(fieldToUpdate, valueToReplaceTheOldOne)
    }

}