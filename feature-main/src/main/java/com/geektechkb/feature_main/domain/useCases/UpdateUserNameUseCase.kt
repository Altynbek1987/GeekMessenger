package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserNameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(name: String) = usersRepository.updateUserName(name)
}