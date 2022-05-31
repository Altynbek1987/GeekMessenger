package com.geektechkb.feature_auth.data.local.preferences

import android.content.SharedPreferences
import com.geektechkb.common.constants.Constants.IS_AUTHORIZED_KEY
import com.geektechkb.common.constants.Constants.VERIFICATION_ID_KEY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthorizePreferences @Inject constructor(private val preferences: SharedPreferences) {

    var isAuthorized: Boolean
        get() = preferences.getBoolean(IS_AUTHORIZED_KEY, false)
        set(value) = preferences.edit().putBoolean(IS_AUTHORIZED_KEY, value).apply()
    var verificationId: String?
        get() = preferences.getString(VERIFICATION_ID_KEY, "")
        set(value) = preferences.edit().putString(IS_AUTHORIZED_KEY, value).apply()
}