package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.common.either.Either
import com.geektechkb.core.typealiases.NotAnActualPagingData
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.models.UserDb
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchPagedUsers(): NotAnActualPagingData
    suspend fun fetchUser(phoneNumber: String): Flow<Either<String, User>>
    fun updateUserStatus(status: String)
    suspend fun updateUserProfileImage(imageFileName: String, byte: ByteArray): String?
    fun getUser(): Flow<List<UserDb>>
    fun insertAllUser(user: UserDb)
    fun updateUser(user: UserDb)
    fun deleteUser(user: UserDb)
}