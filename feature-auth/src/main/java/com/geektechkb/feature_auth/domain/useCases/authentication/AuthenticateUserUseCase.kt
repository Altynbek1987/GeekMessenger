package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String
    ) =
        authRepository.authenticateUser(phoneNumber, name, surname, profileImage)
}