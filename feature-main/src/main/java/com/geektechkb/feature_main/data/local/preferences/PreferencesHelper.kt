package com.geektechkb.feature_main.data.local.preferences

import android.content.SharedPreferences
import com.geektechkb.common.constants.Constants.LANGUAGE_CODE_KEY
import com.geektechkb.common.constants.Constants.LANGUAGE_KEY
import com.geektechkb.feature_main.data.local.Localization
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(
    private val preferences: SharedPreferences
) {
    var language: String?
        get() = preferences.getString(LANGUAGE_KEY, "ru")
        set(value) = preferences.edit().putString(LANGUAGE_KEY, value).apply()
    var languageCode: String?
        get() = preferences.getString(LANGUAGE_CODE_KEY, "ru-RU")
        set(value) = preferences.edit().putString(LANGUAGE_CODE_KEY, value).apply()

    fun setLocale(localization: Localization) {
        preferences.edit().apply {
            putString(LANGUAGE_KEY, localization.language).apply()
            putString(LANGUAGE_CODE_KEY, localization.languageCode).apply()
        }.apply()
    }

    var isLightMode: Boolean
        get() = preferences.getBoolean("theme", false)
        set(value) = preferences.edit().putBoolean("theme", value).apply()
}