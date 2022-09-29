package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class GetCurrentUserPhoneNumberUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke() = usersRepository.getCurrentUserPhoneNumber()
}