package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.common.either.Either
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.typeAliases.NotAnActualPagingData
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchPagedUsers(): NotAnActualPagingData
    suspend fun fetchUser(phoneNumber: String): Flow<Either<String, User>>
}