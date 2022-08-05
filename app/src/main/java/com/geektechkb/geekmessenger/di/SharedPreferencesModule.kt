package com.geektechkb.geekmessenger.di

import android.content.SharedPreferences
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {


    @Singleton
    @Provides
    fun provideName(preferences: SharedPreferences) = PreferencesHelper(preferences)

}
