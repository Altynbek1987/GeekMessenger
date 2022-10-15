package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(
        id: String,
        receiverPhoneNumber: String,
        message: String,
        media: String?,
        mediaType: String? = null,
        videoDuration: String? = null,
        timeMessageWasSent: String,
        messageId: String
    ) =
        messagesRepository.sendMessage(
            id,
            receiverPhoneNumber,
            message,
            media,
            mediaType,
            videoDuration,
            timeMessageWasSent,
            messageId
        )
}