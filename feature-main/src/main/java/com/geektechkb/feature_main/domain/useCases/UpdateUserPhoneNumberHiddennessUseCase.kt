package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserPhoneNumberHiddennessUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(isUserPhoneNumberHidden: Boolean) =
        usersRepository.updateUserNumberHiddenness(isUserPhoneNumberHidden)
}