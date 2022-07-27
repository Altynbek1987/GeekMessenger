package com.geektechkb.feature_main.data.repositories

import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.remote.pagingsources.MessagePagingSource
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore
) : BaseRepository(), MessagesRepository {
    private val messagesRef = firestore.collection(FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH)

    override suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String
    ) {
        getDocument(messagesRef, receiverPhoneNumber)
        addDocument(
            messagesRef,
            hashMapOf(
                "senderPhoneNumber" to id,
                "receiverPhoneNumber" to receiverPhoneNumber,
                "message" to message,
                "timeMessageWasSent" to timeMessageWasSent
            ), id

        )
    }


    override fun fetchPagedMessages() =

        doPagingRequest(MessagePagingSource(messagesRef))
//        Pager(
//            config = PagingConfig(
//                pageSize = 1,
//                prefetchDistance = 1,
//                enablePlaceholders = true,
//                initialLoadSize = 2,
//                maxSize = Int.MAX_VALUE,
//                jumpThreshold = Int.MIN_VALUE
//            ),
//            pagingSourceFactory = { MessagePagingSource(messagesRef) }
//        ).flow


}