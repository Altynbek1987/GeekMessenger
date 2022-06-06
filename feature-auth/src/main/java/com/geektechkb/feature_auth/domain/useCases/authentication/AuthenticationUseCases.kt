package com.geektechkb.feature_auth.domain.useCases.authentication

data class AuthenticationUseCases(
    val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    val provideAuthenticationCallbacksUseCase: ProvideAuthenticationCallbacksUseCase,
    val provideForceResendingTokenUseCase: ProvideForceResendingTokenUseCase,
    val startPhoneNumberVerificationUseCase: StartPhoneNumberVerificationUseCase
)
