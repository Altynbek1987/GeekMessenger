package com.geektechkb.feature_main.domain.models


data class Message(
    val messageId: String? = null,
    val messageKey: String? = null,
    val message: String? = null,
    val mediaResource: String? = null,
    val mediaType: String? = null,
    val videoDuration: String? = null,
    val senderPhoneNumber: String? = null,
    val receiverPhoneNumber: String? = null,
    val timeMessageWasSent: String? = null,
)