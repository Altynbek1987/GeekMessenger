package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.core.typealiases.NotAnActualPagingData

interface MessagesRepository {
    suspend fun sendMessage(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        timeMessageWasSent: String
    )

    fun fetchPagedMessages(): NotAnActualPagingData
}