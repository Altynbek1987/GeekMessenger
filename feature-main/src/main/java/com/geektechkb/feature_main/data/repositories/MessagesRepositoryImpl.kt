package com.geektechkb.feature_main.data.repositories

import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.google.firebase.firestore.CollectionReference
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor(
    private val messagesRef: CollectionReference
) : BaseRepository(), MessagesRepository {


    override suspend fun sendMessage(id: String, message: String, timeMessageWasSent: Long) {
        addDocument(
            messagesRef,
            hashMapOf(
                "senderPhoneNumber" to id,
                "message" to message,
                "timeMessageWasSent" to timeMessageWasSent
            ), id

        )
    }

    override suspend fun fetchMessages() =
        fetchList<Message>(messagesRef)

}