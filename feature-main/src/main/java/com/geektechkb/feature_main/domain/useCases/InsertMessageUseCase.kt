package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class InsertMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
){
    operator fun invoke(message: Message) =
        messagesRepository.insertAllMessage(message)
}