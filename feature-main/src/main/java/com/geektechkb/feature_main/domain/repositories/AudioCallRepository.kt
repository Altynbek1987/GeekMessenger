package com.geektechkb.feature_main.domain.repositories

interface AudioCallRepository {
    fun addAudioCallEventHandler(event: (() -> Unit)? = null)
    fun makeAVoiceCall(
        callerId: String,
        calleeId: String,
        actionOnCallCreatedSuccessfully: (() -> Unit)? = null,
        actionOnCallConnected: (() -> Unit)? = null,
        actionOnCallEnded: (() -> Unit)? = null
    )

    fun acceptAnIncomingCall()
}