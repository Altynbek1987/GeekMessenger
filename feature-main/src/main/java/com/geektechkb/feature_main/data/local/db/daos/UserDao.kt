package com.geektechkb.feature_main.data.local.db.daos

import androidx.room.*
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.models.UserDb
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM UserDb")
    fun getUser(): Flow<List<UserDb>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAllUsers(user: UserDb)

    @Delete
    suspend fun deleteUser(user: UserDb)

    @Update
    suspend fun updateUser(user: UserDb)
}