package com.geektechkb.feature_main.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geektechkb.feature_main.data.local.db.daos.MessageDao
import com.geektechkb.feature_main.domain.models.User

@Database(entities = [User::class] , version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun messageDao(): MessageDao
}