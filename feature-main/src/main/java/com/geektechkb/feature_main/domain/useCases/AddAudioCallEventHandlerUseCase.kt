package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.AudioCallRepository
import javax.inject.Inject

class AddAudioCallEventHandlerUseCase @Inject constructor(
    private val audioCallRepository: AudioCallRepository
) {
    operator fun invoke(event: (() -> Unit)? = null) =
        audioCallRepository.addAudioCallEventHandler(event)
}