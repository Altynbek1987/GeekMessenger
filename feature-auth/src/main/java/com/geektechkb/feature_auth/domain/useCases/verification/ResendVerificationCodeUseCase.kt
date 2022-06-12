package com.geektechkb.feature_auth.domain.useCases.verification

import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualActivity
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualCallbacks
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualForceResendingToken
import javax.inject.Inject

class ResendVerificationCodeUseCase @Inject constructor(
    private val codeVerificationRepository: CodeVerificationRepository
) {
    operator fun invoke(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualForceResendingToken: NotAnActualForceResendingToken?
    ) = codeVerificationRepository.resendVerificationCode(
        notAnActualFirebaseAuth,
        phoneNumber,
        notAnActualActivity,
        notAnActualCallbacks,
        notAnActualForceResendingToken
    )
}