package com.geektechkb.feature_main.data.repositories

import android.net.Uri
import com.geektechkb.common.constants.Constants.FIREBASE_CLOUD_STORAGE_MESSAGE_IMAGES_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH
import com.geektechkb.common.constants.Constants.FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.core.extensions.snapshotFlow
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.flow.map
import java.io.FileNotFoundException
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
    cloudStorage: FirebaseStorage,
) : BaseRepository(), MessagesRepository {
    private val messagesRef = firestore.collection(FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH)
    private val cloudStorageRef = cloudStorage.reference

    override suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        media: String?,
        mediaType: String?,
        videoDuration: String?,
        timeMessageWasSent: String,
        messageId: String,
    ) {
        try {
            addDocument(
                messagesRef,
                hashMapOf(
                    "messageId" to messageId,
                    "messageKey" to (id + receiverPhoneNumber),
                    "message" to message,
                    "mediaResource" to uploadUncompressedMediaToCloudStorage(
                        cloudStorageRef,
                        Uri.parse(media),
                        FIREBASE_CLOUD_STORAGE_MESSAGE_IMAGES_PATH, generateRandomId()
                    ),
                    "mediaType" to mediaType,
                    "videoDuration" to videoDuration,
                    "senderPhoneNumber" to id,
                    "receiverPhoneNumber" to receiverPhoneNumber,
                    "timeMessageWasSent" to timeMessageWasSent
                )
            )
        } catch (e: StorageException) {
            addDocument(
                messagesRef,
                hashMapOf(
                    "messageId" to messageId,
                    "messageKey" to (id + receiverPhoneNumber),
                    "message" to message,
                    "mediaResource" to media,
                    "mediaType" to mediaType,
                    "videoDuration" to videoDuration,
                    "senderPhoneNumber" to id,
                    "receiverPhoneNumber" to receiverPhoneNumber,
                    "timeMessageWasSent" to timeMessageWasSent
                )
            )
        } catch (e: FileNotFoundException) {
            addDocument(
                messagesRef,
                hashMapOf(
                    "messageId" to messageId,
                    "messageKey" to (id + receiverPhoneNumber),
                    "message" to message,
                    "mediaResource" to media,
                    "mediaType" to mediaType,
                    "videoDuration" to videoDuration,
                    "senderPhoneNumber" to id,
                    "receiverPhoneNumber" to receiverPhoneNumber,
                    "timeMessageWasSent" to timeMessageWasSent
                )
            )
        }
    }

    override fun fetchPagedMessages(senderPhoneNumber: String, receiverPhoneNumber: String) =
        messagesRef
            .whereIn("messageKey", listOf(senderPhoneNumber + receiverPhoneNumber, receiverPhoneNumber + senderPhoneNumber))
            .orderBy(FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT)
            .limitToLast(10000)
            .snapshotFlow()
            .map { list ->
                list.map { document ->
                    document.toObject(Message::class.java)
                }
            }
}