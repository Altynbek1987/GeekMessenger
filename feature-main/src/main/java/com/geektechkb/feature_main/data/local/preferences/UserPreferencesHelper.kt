package com.geektechkb.feature_main.data.local.preferences

import android.content.SharedPreferences
import com.geektechkb.common.constants.Constants.CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesHelper @Inject constructor(private val preferences: SharedPreferences) {


    var currentUserPhoneNumber: String
        get() = preferences.getString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, "").toString()
        set(value) = preferences.edit().putString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, value)
            .apply()
}