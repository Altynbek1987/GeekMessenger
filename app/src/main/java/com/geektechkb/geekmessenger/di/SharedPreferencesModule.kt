package com.geektechkb.geekmessenger.di

import android.content.Context
import android.content.SharedPreferences
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("geektech.preferences", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideAuthorizePreferences(preferences: SharedPreferences) =
        AuthorizePreferences(preferences)

    @Singleton
    @Provides
    fun provideOnBoardPreferences(preferences: SharedPreferences) =
        OnBoardPreferencesHelper(preferences)

    @Singleton
    @Provides
    fun provideUserPreferences(preferences: SharedPreferences) =
        UserPreferencesHelper(preferences)

    @Singleton
    @Provides
    fun provideName(preferences: SharedPreferences) = PreferencesHelper(preferences)
}