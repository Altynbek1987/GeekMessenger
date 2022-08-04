package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    operator fun invoke() =
        messagesRepository.getMessage()
}