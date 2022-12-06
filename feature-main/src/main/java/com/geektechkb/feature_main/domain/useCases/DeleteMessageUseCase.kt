package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import javax.inject.Inject

class DeleteMessageUseCase @Inject constructor(
    private val messageRepository: MessagesRepository
) {
    operator fun invoke(messageId: String) = messageRepository.deleteMessage(messageId)
}