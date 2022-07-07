package com.geektechkb.feature_main.themes

import android.content.Context
import androidx.core.content.ContextCompat
import com.geektechkb.feature_main.R

class NightTheme : MyAppTheme {

    companion object {
        val ThemeId = 1
    }

    override fun id(): Int {
        return ThemeId
    }

    override fun activityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun activityImageRes(context: Context): Int {
        return R.color.black
    }

    override fun activityIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun activityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }

    override fun activityThemeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.black)
    }
}