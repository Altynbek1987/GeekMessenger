package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.feature_main.domain.models.SentMessage

interface MessagesRepository {
    fun sendMessage(message: SentMessage)
    fun getMessages(messages: List<SentMessage>)
}