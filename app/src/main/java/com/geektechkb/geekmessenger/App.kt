package com.geektechkb.geekmessenger

import android.app.Application
import android.content.Context
import android.util.Log
import com.geektechkb.feature_main.data.local.preferences.LocaleHelper
import com.google.firebase.messaging.FirebaseMessaging
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        installEmojiProvider()

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@addOnCompleteListener
            }
            val token = task.result
            Log.e("RUSS", "Token -> $token")
        }
    }

    private fun installEmojiProvider() {
        EmojiManager.install(GoogleEmojiProvider())
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper(base).loadLocale(base))
    }
}