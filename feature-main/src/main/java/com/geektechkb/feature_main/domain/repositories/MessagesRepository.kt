package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.feature_main.domain.models.Message

interface MessagesRepository {
    suspend fun sendMessage(id: String, message: String, timeMessageWasSent: Long)
    suspend fun fetchMessages(): List<Message>
}