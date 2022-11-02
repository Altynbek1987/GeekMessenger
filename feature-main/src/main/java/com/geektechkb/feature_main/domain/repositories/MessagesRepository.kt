package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.feature_main.domain.models.Message
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        media: String?,
        mediaType: String? = null,
        videoDuration: String? = null,
        timeMessageWasSent: String,
        messageId: String,
    )

    fun fetchPagedMessages(
        senderPhoneNumber: String,
        receiverPhoneNumber: String
    ): Flow<List<Message?>>
}