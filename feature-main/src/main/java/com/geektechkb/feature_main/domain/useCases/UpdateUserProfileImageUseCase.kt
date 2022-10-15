package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UpdateUserProfileImageUseCase @Inject constructor(
    private val repository: UsersRepository
) {
    suspend operator fun invoke(url: String) =
        repository.updateUserProfileImage(url)
}