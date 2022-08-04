package com.geektechkb.feature_main.data.local.db.daos

import androidx.room.*
import com.geektechkb.feature_main.domain.models.Message
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getMessage(): Flow<List<Message>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertMessage(message: Message)

    @Delete
    suspend fun deleteMessage(message: Message)

    @Update
    suspend fun updateMessage(message: Message)
}