package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.core.typealiases.NotAnActualPagingData

interface MessagesRepository {
    suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String,
        messageId: String
    )
    suspend fun sendVoiceMessage(file : String,voiceFileName :String)

    fun fetchPagedMessages(): NotAnActualPagingData
    suspend fun setupOneOnOneChat(
        id: String?,
        firstChatterPhoneNumber: String,
        secondChatterPhoneNumber: String
    ): String?
}