package com.geektechkb.geekmessenger.di

import android.content.Context
import android.content.SharedPreferences
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
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
}