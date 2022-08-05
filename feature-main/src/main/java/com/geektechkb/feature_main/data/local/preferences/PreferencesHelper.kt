package com.geektechkb.core.data.local.preferences

import Localization
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesHelper @Inject constructor(
    private val preferences: SharedPreferences
) {

    fun getLanguage() = preferences.getString("language", "ru")

    fun getLanguageCode() = preferences.getString("language_code", "ru-RU")

    fun setLocale(localization: Localization) {
        preferences.edit().apply {
            putString("language", localization.language).apply()
            putString("language_code", localization.languageCode).apply()
        }
    }

}