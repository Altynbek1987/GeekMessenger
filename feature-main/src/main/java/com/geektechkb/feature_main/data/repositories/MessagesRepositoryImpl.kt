package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_CHATTERS_KEY
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.remote.pagingsources.MessagePagingSource
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage
) : BaseRepository(), MessagesRepository {
    private val messagesRef =
        firestore.collection(FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH)
    private val sortedMessages =
        messagesRef.orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT, Query.Direction.ASCENDING)
    private val voiceRef = cloudStorage.reference

    override suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String,
        messageId: String
    ) {
        addDocument(
            messagesRef,
            hashMapOf(
                "senderPhoneNumber" to id,
                "receiverPhoneNumber" to receiverPhoneNumber,
                "messages" to message,
                "timeMessageWasSent" to timeMessageWasSent
            ), messageId

        )
    }


    override fun fetchPagedMessages() =

        doPagingRequest(MessagePagingSource(sortedMessages))

    override suspend fun setupOneOnOneChat(
        id: String?,
        firstChatterPhoneNumber: String,
        secondChatterPhoneNumber: String
    ): String? {
        addDocument(
            messagesRef,
            hashMapOf(
                FIREBASE_FIRESTORE_CHATTERS_KEY to listOf(
                    firstChatterPhoneNumber,
                    secondChatterPhoneNumber
                )
            ), id
        )
        return id
    }


    suspend fun sendVoiceMessageToCloudStorage(file: Uri?, voiceFileName: String) =
        file?.let {
            voiceRef
                .child("voiceMessages/")
                .child(voiceFileName)
                .putFile(it)
                .await()
                .storage
                .downloadUrl
                .await()
                .toString()
        }
}