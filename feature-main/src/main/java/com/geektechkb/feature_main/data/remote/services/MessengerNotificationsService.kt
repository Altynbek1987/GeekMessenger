package com.geektechkb.feature_main.data.remote.services

import android.content.Intent
import android.util.Log
import com.geektechkb.common.constants.Constants.INTENT_FILTER
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging

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

    companion object {
        fun subscribeToTopic(vararg topics: String) {
            for (topic in topics)
                Firebase.messaging.subscribeToTopic(topic).addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (!task.isSuccessful)
                            return@OnCompleteListener
                        Log.d("Push", task.isSuccessful.toString())
                    })

        }

        fun unsubscribeFromTopic(vararg topics: String) {
            for (topic in topics)
                Firebase.messaging.unsubscribeFromTopic(topic)
                    .addOnCompleteListener(OnCompleteListener { task ->
                        if (!task.isSuccessful)
                            return@OnCompleteListener
                        Log.d("Push", task.isSuccessful.toString())
                    })
        }
    }
}