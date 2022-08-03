package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.common.either.Either
import com.geektechkb.core.typealiases.NotAnActualPagingData
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchPagedUsers(): NotAnActualPagingData
    suspend fun fetchUser(phoneNumber: String): Flow<Either<String, User>>
    fun updateUserStatus(status: String)
    fun getUsers(): Flow<List<User>>
    fun insertAllUser(user: User)
    fun updateUsers(user: User)
    fun deleteUsers(user: User)
}