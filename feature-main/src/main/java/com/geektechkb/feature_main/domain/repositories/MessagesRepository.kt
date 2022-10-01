package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.feature_main.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        image :String,
        timeMessageWasSent: String,
        messageId: String,
    )

    suspend fun sendVoiceMessage(file: String, voiceFileName: String)


    fun fetchPagedMessages(
        senderPhoneNumber: String,
        receiverPhoneNumber: String
    ): Flow<List<Message?>>
}