package com.geektechkb.feature_main.data.repositories

import android.util.Log
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_main.domain.repositories.AudioCallRepository
import com.sendbird.calls.AuthenticateParams
import com.sendbird.calls.SendBirdCall

class AudioCallRepositoryImpl : BaseRepository(), AudioCallRepository {
    override fun authenticateToSendbird(userId: String, accessToken: String) {
        val params = AuthenticateParams(userId).setAccessToken(accessToken)
        SendBirdCall.authenticate(
            params
        ) { user, e ->
            when (e) {
                null -> null
                else -> Log.e("tag", "${user.toString()} has successfully authenticated")
            }
        }
    }
}