package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import javax.inject.Inject

class ProvideAuthenticationCallbacksUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        authenticationSucceeded: ((() -> Unit))? = null,
        authInvalidCredentialsError: ((() -> Unit))? = null,
        tooManyRequestsError: ((() -> Unit))? = null,
    ) = authRepository.provideAuthenticationCallbacks(authenticationSucceeded = {
        authenticationSucceeded?.invoke()
    }, authInvalidCredentialsError = { authInvalidCredentialsError }, tooManyRequestsError = {
        tooManyRequestsError?.invoke()
    })
}