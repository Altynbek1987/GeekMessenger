package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import com.geektechkb.common.constants.Constants.FIREBASE_CLOUD_STORAGE_VOICE_MESSAGES_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.snapshotFlow
import com.geektechkb.feature_main.data.local.db.daos.MessageDao
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messageDao: MessageDao,
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,
) : BaseRepository(), MessagesRepository {
    private val messagesRef =
        firestore.collection(FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH)
    private val voiceRef = cloudStorage.reference
    private val cloudStorageRef = cloudStorage.reference
    private val messageMap = hashMapOf<String, Any?>()

    override suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        messageMap["messageId"] = messageId
        messageMap["message"] = message
        messageMap["senderPhoneNumber"] = id
        messageMap["receiverPhoneNumber"] = receiverPhoneNumber
        messageMap["timeMessageWasSent"] = timeMessageWasSent
        addDocument(
            messagesRef,
            messageMap,
            messageId
        )
    }

    override suspend fun sendVoiceMessage(file: String, voiceFileName: String) {
        uploadVoiceMessageToCloudStorage(
            cloudStorageRef, Uri.parse(file),
            FIREBASE_CLOUD_STORAGE_VOICE_MESSAGES_PATH, voiceFileName
        )
    }

    override fun fetchPagedMessages() = messagesRef
        .orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT)
        .limitToLast(10)
        .snapshotFlow()
        .map { list ->
            list.map { document ->
                document.toObject(Message::class.java)
            }
        }

        //Room
    override fun getMessage(): Flow<List<Message>> {
        TODO("Not yet implemented")
    }

    override fun insertAllMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun updateMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(message: Message) {
        TODO("Not yet implemented")
    }

//        override suspend fun setupOneOnOneChat(
//            id: String?,
//            firstChatterPhoneNumber: String,
//            secondChatterPhoneNumber: String,
//        ): String? {
//            addDocument(
//                messagesRef,
//                hashMapOf(
//                    FIREBASE_FIRESTORE_CHATTERS_KEY to listOf(
//                        firstChatterPhoneNumber,
//                        secondChatterPhoneNumber
//
//                    )
//                ), id
//            )
//            return id
//        }


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