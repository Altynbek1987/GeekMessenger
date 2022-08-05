package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.AudioCallRepository
import javax.inject.Inject

class MakeAVoiceCallUseCase @Inject constructor(
    private val audioCallRepository: AudioCallRepository
) {
    operator fun invoke(
        callerId: String,
        calleeId: String,
        actionOnCallCreatedSuccessfully: (() -> Unit)? = null,
        actionOnCallConnected: (() -> Unit)? = null,
        actionOnCallEnded: (() -> Unit)? = null
    ) = audioCallRepository.makeAVoiceCall(
        callerId,
        calleeId,
        actionOnCallCreatedSuccessfully,
        actionOnCallConnected,
        actionOnCallEnded
    )
}