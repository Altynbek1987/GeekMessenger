package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(id: String, message: String, timeMessageWasSent: Long) =
        messagesRepository.sendMessage(id, message, timeMessageWasSent)
}