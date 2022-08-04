package com.geektechkb.feature_main.data.local.db.daos

import androidx.room.*
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM message")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertUser(user: User)

    @Delete
    suspend fun deleteUsers(user: User)

    @Update
    suspend fun updateUsers(user: User)
}