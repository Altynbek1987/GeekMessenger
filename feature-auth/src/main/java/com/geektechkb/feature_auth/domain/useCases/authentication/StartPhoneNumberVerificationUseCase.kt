package com.geektechkb.feature_auth.domain.useCases.authentication

import com.geektechkb.core.typealiases.NotAnActualActivity
import com.geektechkb.core.typealiases.NotAnActualCallbacks
import com.geektechkb.core.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
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