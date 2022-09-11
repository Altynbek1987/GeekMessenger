package com.geektechkb.geekmessenger.di

import android.content.Context
import android.content.SharedPreferences
import com.geektechkb.feature_main.data.local.db.AppDataBase
import com.geektechkb.feature_main.data.local.db.RoomClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context) =
        RoomClient().provideCreateAppDataBase(context)

    @Singleton
    @Provides
    fun provideUserDao(appDataBase: AppDataBase) =
        RoomClient().provideUserDao(appDataBase)
}