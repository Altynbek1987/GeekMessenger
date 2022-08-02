package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.MessagesRepository
import javax.inject.Inject

class SendVoiceMessageUseCase @Inject constructor(
    private val messagesRepository: MessagesRepository
) {
    suspend operator fun invoke(file: String, voiceFileName: String) =
        messagesRepository.sendVoiceMessage(file, voiceFileName)
}