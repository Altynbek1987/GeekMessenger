package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.models.UserDb
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(user: UserDb) =
        usersRepository.updateUser(user)
}