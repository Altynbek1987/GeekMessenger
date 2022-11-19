package com.geektechkb.feature_main.data.local.preferences

import android.content.Context
import com.geektechkb.common.constants.Constants.LANGUAGE_KEY
import java.util.*

class LocaleHelper(
    context: Context
) {
    private var preferences =
        context.getSharedPreferences("geektech.preferences", Context.MODE_PRIVATE)

    fun loadLocale(context: Context): Context {
        val locale = Locale(preferences.getString(LANGUAGE_KEY, "ru").toString())
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }
}