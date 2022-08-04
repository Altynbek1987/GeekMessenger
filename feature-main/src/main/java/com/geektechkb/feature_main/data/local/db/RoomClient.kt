package com.geektechkb.feature_main.data.local.db

import android.content.Context
import androidx.room.Room
import com.geektechkb.feature_main.data.local.db.daos.MessageDao
import dagger.hilt.android.qualifiers.ApplicationContext

class RoomClient {
    fun provideCreateAppDataBase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            AppDataBase::class.java,
            "geekmessenger_database"
        ).fallbackToDestructiveMigration()
            .build()

    fun provideMessageDao(userDataBase: AppDataBase): MessageDao = userDataBase.messageDao()
}