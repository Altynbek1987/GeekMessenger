package com.geektechkb.feature_main.data.remote.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.presentation.ui.fragments.chat.ChatFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.firebase.messaging.ktx.messaging
import kotlin.random.Random

class MessengerNotificationsService : FirebaseMessagingService() {

	val CHANNWL_ID = "my_notification_channel"

	override fun onMessageReceived(message: RemoteMessage) {
		super.onMessageReceived(message)
		showPushNotifation(message)
	}


	override fun onNewToken(tokenUser: String) {
		super.onNewToken(tokenUser)
		token = tokenUser
	}

	@SuppressLint("UnspecifiedImmutableFlag")
	private fun showPushNotifation(message: RemoteMessage) {
		val intent = Intent(this, ChatFragment::class.java)
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
		val pendingIntent = PendingIntent.getActivity(
			this, 0, intent, FLAG_ONE_SHOT
		)


		val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)
		val notificationBuilder = NotificationCompat.Builder(this, CHANNWL_ID)
			.setContentTitle(message.data["title"])
			.setContentText(message.data["body"])
			.setSmallIcon(R.drawable.arrow)
			.setAutoCancel(true)
			.setSound(defaultSoundUri)
			.setContentIntent(pendingIntent)

		val notificationManager =
			getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
		val notificationId = Random.nextInt()

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val channel = NotificationChannel(
				CHANNWL_ID,
				getString(R.string.chat_notifications),
				NotificationManager.IMPORTANCE_DEFAULT
			)

			notificationManager.createNotificationChannel(channel)
		}
		notificationManager.notify(notificationId, notificationBuilder.build())
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

    companion object {
        var sharePref: SharedPreferences? = null
        var token: String?
            get() {
                return sharePref?.getString("token", "")
            }
            set(value) {
                sharePref?.edit()?.putString("token", value)?.apply()
            }

		fun subscribeToTopic(topics: String) {
			for (topic in topics)
				Firebase.messaging.subscribeToTopic(topic.toString()).addOnCompleteListener(
					OnCompleteListener { task ->
						if (!task.isSuccessful)
							return@OnCompleteListener
						Log.d("Push", task.isSuccessful.toString())
					})

		}

		fun unsubscribeFromTopic(topics: String) {
			for (topic in topics)
				Firebase.messaging.unsubscribeFromTopic(topic.toString())
					.addOnCompleteListener(OnCompleteListener { task ->
						if (!task.isSuccessful)
							return@OnCompleteListener
						Log.d("Push", task.isSuccessful.toString())
					})
		}
    }
}