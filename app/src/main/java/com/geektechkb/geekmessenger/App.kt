package com.geektechkb.geekmessenger

import android.app.Application
import android.util.Log
import com.geektechkb.common.constants.Constants.UNIQUE_HANDLER_ID
import com.geektechkb.feature_main.BuildConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.sendbird.calls.DirectCall
import com.sendbird.calls.SendBirdCall
import com.sendbird.calls.handler.SendBirdCallListener
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val emojiManager = EmojiManager.install(GoogleEmojiProvider())

    override fun onCreate() {
        super.onCreate()
        initSendBird()
        installEmojiProvider()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {return@addOnCompleteListener }
            val token = task.result
            Log.e("RUSS", "Token -> $token")}
        SendBirdCall.addListener(UNIQUE_HANDLER_ID, object : SendBirdCallListener() {
            override fun onRinging(call: DirectCall) {
            }

        })
    }

    private fun installEmojiProvider() {
        EmojiManager.install(GoogleEmojiProvider())
    }

    private fun initSendBird() {
        SendBirdCall.init(
            applicationContext,
            BuildConfig.SENDBIRD_APP_ID
        )
    }
}