package com.geektechkb.feature_main.themes

import android.content.Context
import androidx.core.content.ContextCompat
import com.geektechkb.feature_main.R

class LightTheme : MyAppTheme {

    companion object {
        val ThemeId = 0
    }

    override fun id(): Int {
        return ThemeId
    }

    override fun activityBackgroundColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun activityImageRes(context: Context): Int {
        return R.color.white
    }

    override fun activityIconColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun activityTextColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }

    override fun activityThemeButtonColor(context: Context): Int {
        return ContextCompat.getColor(context, R.color.white)
    }
}