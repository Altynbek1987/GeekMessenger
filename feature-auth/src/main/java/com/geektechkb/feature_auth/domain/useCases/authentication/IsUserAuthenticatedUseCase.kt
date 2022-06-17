package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import javax.inject.Inject

class IsUserAuthenticatedUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.isUserAuthenticated()
}