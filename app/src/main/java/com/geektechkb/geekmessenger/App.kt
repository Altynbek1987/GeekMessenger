package com.geektechkb.geekmessenger

import android.app.Application
import com.geektechkb.feature_main.BuildConfig
import com.sendbird.calls.SendBirdCall
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val emojiManager = EmojiManager.install(GoogleEmojiProvider())
    override fun onCreate() {
        SendBirdCall.init(applicationContext, BuildConfig.SENDBIRD_APP_ID)
        super.onCreate()
    }
}