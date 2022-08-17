package com.geektechkb.feature_main.data.remote.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.presentation.ui.fragments.home.HomeFragment
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MessengerNotificationsService : FirebaseMessagingService() {

    val CHANNWL_ID = "my_notification_channel"

    companion object {
        var sharePref: SharedPreferences? = null
        var token: String?
            get() {
                return sharePref?.getString("token", "")
            }
            set(value) {
                sharePref?.edit()?.putString("token", value)?.apply()
            }
    }

    override fun onNewToken(tokenUser: String) {
        super.onNewToken(tokenUser)
        token = tokenUser
    }

//        sendBroadcast(intent)

    //    }
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, HomeFragment::class.java)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = Random.nextInt()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification = NotificationCompat.Builder(this, CHANNWL_ID)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setSmallIcon(R.drawable.arrow)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()
        notificationManager.notify(notificationId, notification)

//        val intentww = Intent(INTENT_FILTER)
//        message.data.forEach { (key, value) ->
//            intent.putExtra(key, value)
//
//
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channelName = "ChannelFirebaseChat"
        val channel = NotificationChannel(CHANNWL_ID, channelName, IMPORTANCE_HIGH).apply {
            description = "My FIREBASE CHAT DESCRIPTION"
            enableLights(true)
            lightColor = Color.WHITE
        }
        notificationManager.createNotificationChannel(channel)
    }
}