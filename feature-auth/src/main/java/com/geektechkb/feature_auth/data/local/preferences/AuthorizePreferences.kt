package com.geektechkb.feature_auth.data.local.preferences

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizePreferences @Inject constructor(private val preferences: SharedPreferences) {

    var isAuthorize: Boolean
        get() = preferences.getBoolean("isAuthorize", false)
        set(value) = preferences.edit().putBoolean("isAuthorize", value).apply()
}