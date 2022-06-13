package com.geektechkb.feature_auth.data.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnBoardPreferencesHelper @Inject constructor(private val preferences: SharedPreferences) {

    var hasOnBoardBeenShown: Boolean
        get() = preferences.getBoolean("hasBeenShown", false)
        set(value) = preferences.edit().putBoolean("hasBeenShown", value).apply()
}