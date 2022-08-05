package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.AudioCallRepository
import javax.inject.Inject

class AcceptAnIncomingCallUseCase @Inject constructor(
    private val audioCallRepository: AudioCallRepository
) {
    operator fun invoke() = audioCallRepository.acceptAnIncomingCall()
}