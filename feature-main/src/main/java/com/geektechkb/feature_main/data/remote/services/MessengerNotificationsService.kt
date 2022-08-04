package com.geektechkb.feature_main.data.remote.services

import android.content.Intent
import com.geektechkb.common.constants.Constants.INTENT_FILTER
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessengerNotificationsService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = Intent(INTENT_FILTER)
        message.data.forEach { (key, value) ->
            intent.putExtra(key, value)
        }

        sendBroadcast(intent)
    }
}