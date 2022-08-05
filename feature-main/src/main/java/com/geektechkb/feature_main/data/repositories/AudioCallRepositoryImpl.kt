package com.geektechkb.feature_main.data.repositories

import android.util.Log
import com.geektechkb.common.constants.Constants.UNIQUE_HANDLER_ID
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.feature_main.domain.repositories.AudioCallRepository
import com.sendbird.calls.*
import com.sendbird.calls.handler.DirectCallListener
import com.sendbird.calls.handler.SendBirdCallListener
import java.util.*
import javax.inject.Inject

class AudioCallRepositoryImpl @Inject constructor() : BaseRepository(), AudioCallRepository {

    private fun authenticateAUserToVoiceCall(callerId: String) {
        val callerParams = AuthenticateParams(callerId)
        SendBirdCall.authenticate(
            callerParams
        ) { user, e ->
            if (e == null) {
                Log.e("gaypopSuccess", "${user?.userId} has authenticated successfully")
                registerPushToken()
            } else {
                Log.e("gaypopFuckingError", e.message.toString())
            }


        }


    }

    private fun registerPushToken() {
        SendBirdCall.registerPushToken(
            generateRandomId(), true
        ) { e ->
            if (e == null)
                Log.e("gaypop", "PushToken has been created successfully")
            else
                Log.e("gaypopError", e.message.toString())
        }
    }

    override fun addAudioCallEventHandler(event: (() -> Unit)?) {
        var callId: String? = ""
        SendBirdCall.addListener(UUID.randomUUID().toString(), object : SendBirdCallListener() {
            override fun onRinging(call: DirectCall) {
                event?.invoke()
            }
        })
    }

    override fun makeAVoiceCall(
        callerId: String, calleeId: String, actionOnCallCreatedSuccessfully: (() -> Unit)?,
        actionOnCallConnected: (() -> Unit)?,
        actionOnCallEnded: (() -> Unit)?
    ) {
        authenticateAUserToVoiceCall(callerId)


        val dialParams = DialParams(calleeId)
        val callOptions = CallOptions().setFrontCameraAsDefault(true).setAudioEnabled(true)
        dialParams.setCallOptions(callOptions)
        val call: DirectCall? = SendBirdCall.dial(
            dialParams
        ) { call, e ->
            if (e == null) {
                actionOnCallCreatedSuccessfully?.invoke()
                call?.accept(AcceptParams().setCallOptions(callOptions))

            } else {
                Log.e("gaypopError3", e.message.toString())
            }

        }
        call?.setListener(object : DirectCallListener() {
            override fun onConnected(call: DirectCall) {
                actionOnCallConnected?.invoke()
            }

            override fun onEnded(call: DirectCall) {
                actionOnCallEnded?.invoke()
            }

        })
    }

    override fun acceptAnIncomingCall() {
        SendBirdCall.addListener(UNIQUE_HANDLER_ID, object : SendBirdCallListener() {
            override fun onRinging(call: DirectCall) {
                call.setListener(object : DirectCallListener() {
                    override fun onConnected(call: DirectCall) {
                    }

                    override fun onEnded(call: DirectCall) {
                    }

                })
                call.accept(AcceptParams())
            }

        })

    }
}