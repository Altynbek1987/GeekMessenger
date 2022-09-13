package com.geektechkb.feature_main.data.local.preferences

import android.content.Context
import java.util.*
import javax.inject.Inject

class LocaleHelper @Inject constructor(
    private val preferencesHelper: PreferencesHelper
) {
    fun loadLocale(context: Context): Context {
        val locale = Locale(preferencesHelper.language.toString())
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}