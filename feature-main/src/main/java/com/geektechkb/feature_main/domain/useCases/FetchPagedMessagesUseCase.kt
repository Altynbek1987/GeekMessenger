package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import javax.inject.Inject

class FetchPagedMessagesUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke() = messagesRepository.fetchPagedMessages()
}