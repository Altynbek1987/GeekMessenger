package com.geektechkb.feature_auth.domain.useCases.verification

import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualActivity
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualPhoneAuthCredential
import javax.inject.Inject

class SignInWithPhoneAuthCredentialUseCase @Inject constructor(
    private val codeVerificationRepository: CodeVerificationRepository
) {
    operator fun invoke(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        credential: NotAnActualPhoneAuthCredential,
        notAnActualActivity: NotAnActualActivity,
        userSuccessfullyVerifiedTheirPhoneNumber: ((() -> Unit))?,
        authenticationProcessFailed: ((() -> Unit))?,
        ifUserHasEnteredInvalidCredentials: ((() -> Unit))?

    ) = codeVerificationRepository.signInWithPhoneAuthCredential(
        notAnActualFirebaseAuth,
        credential,
        notAnActualActivity,
        userSuccessfullyVerifiedTheirPhoneNumber = {

            userSuccessfullyVerifiedTheirPhoneNumber?.invoke()
        }, authenticationProcessFailed = {

            authenticationProcessFailed?.invoke()
        }, ifUserHasEnteredInvalidCredentials = {
            ifUserHasEnteredInvalidCredentials?.invoke()
        }
    )
}