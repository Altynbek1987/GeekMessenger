package com.geektechkb.feature_main.domain.models

import androidx.room.Entity

@Entity
data class Message(
    val messageId: String? = null,
    val message: String? = null,
    val senderPhoneNumber: String? = null,
    val receiverPhoneNumber: String? = null,
    val timeMessageWasSent: String? = null,
)
