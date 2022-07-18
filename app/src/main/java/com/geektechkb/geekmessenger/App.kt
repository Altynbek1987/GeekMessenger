package com.geektechkb.geekmessenger

import android.app.Application
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    val emojiManager = EmojiManager.install(GoogleEmojiProvider())
}