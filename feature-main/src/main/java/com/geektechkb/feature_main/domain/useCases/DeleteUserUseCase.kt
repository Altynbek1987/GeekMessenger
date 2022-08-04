package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.models.UserDb
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(user: UserDb) =
        usersRepository.deleteUser(user)
}