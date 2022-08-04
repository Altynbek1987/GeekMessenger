package com.geektechkb.feature_main.data.local.db.daos

import androidx.room.*
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getUser(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllUsers(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)
}