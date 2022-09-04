package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserLastNameUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(lastName: String) = usersRepository.updateUserLastName(lastName)
}