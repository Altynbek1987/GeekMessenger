package com.geektechkb.feature_auth.data.local.preferences

import android.content.SharedPreferences
import com.geektechkb.common.constants.Constants.HAS_ONBOARD_BEEN_SHOWN_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnBoardPreferencesHelper @Inject constructor(private val preferences: SharedPreferences) {

    var hasOnBoardBeenShown: Boolean
        get() = preferences.getBoolean(HAS_ONBOARD_BEEN_SHOWN_KEY, false)
        set(value) = preferences.edit().putBoolean(HAS_ONBOARD_BEEN_SHOWN_KEY, value).apply()


}