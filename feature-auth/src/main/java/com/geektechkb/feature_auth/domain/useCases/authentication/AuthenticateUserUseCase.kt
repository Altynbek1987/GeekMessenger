package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import javax.inject.Inject

class AuthenticateUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        lastSeen: String,
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String?,
        imageFileName: String,
    ) =
        authRepository.authenticateUser(
            lastSeen,
            phoneNumber,
            name,
            surname,
            profileImage,
            imageFileName,
        )
}