package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualActivity
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualCallbacks
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualFirebaseAuth
import javax.inject.Inject

class StartPhoneNumberVerificationUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth, phoneNumber: String,
        notAnActualActivity: NotAnActualActivity, notAnActualCallbacks: NotAnActualCallbacks
    ) = authRepository.startPhoneNumberVerification(
        notAnActualFirebaseAuth,
        phoneNumber,
        notAnActualActivity,
        notAnActualCallbacks
    )
}