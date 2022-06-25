package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.data.remote.pagingsources.MessagePagingSource
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage
) : BaseRepository(), MessagesRepository {
    private val messagesRef = firestore.collection(FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH)
    private val voiceRef = cloudStorage.reference

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