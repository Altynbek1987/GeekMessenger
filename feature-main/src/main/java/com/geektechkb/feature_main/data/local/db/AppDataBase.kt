package com.geektechkb.feature_main.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.geektechkb.feature_main.data.local.db.daos.UserDao
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.models.UserDb

@Database(entities = [UserDb::class] , version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun userDao(): UserDao
}