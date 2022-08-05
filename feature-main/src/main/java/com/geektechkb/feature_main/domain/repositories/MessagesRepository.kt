package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.flow.Flow

interface MessagesRepository {
    suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String,
        messageId: String,
    )

    suspend fun sendVoiceMessage(file: String, voiceFileName: String)

    fun fetchPagedMessages(): Flow<List<Message?>>

//    suspend fun setupOneOnOneChat(
//        id: String?,
//        firstChatterPhoneNumber: String,
//        secondChatterPhoneNumber: String,
//    ): String?
}