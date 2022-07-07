package com.geektechkb.feature_main.themes

import android.content.Context
import com.geektechkb.core.utils.AppTheme

interface MyAppTheme : AppTheme {
    fun activityBackgroundColor(context: Context): Int
    fun activityImageRes(context: Context): Int
    fun activityIconColor(context: Context): Int
    fun activityTextColor(context: Context): Int
    fun activityThemeButtonColor(context: Context): Int
}