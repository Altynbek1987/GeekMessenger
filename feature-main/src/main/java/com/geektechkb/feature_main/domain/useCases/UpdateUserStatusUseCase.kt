package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserStatusUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend operator fun invoke(status: String) = usersRepository.updateUserStatus(status)
}