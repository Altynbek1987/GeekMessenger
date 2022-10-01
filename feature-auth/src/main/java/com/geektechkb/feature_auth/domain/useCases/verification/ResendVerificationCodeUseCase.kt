package com.geektechkb.feature_auth.domain.useCases.verification

import com.geektechkb.core.typealiases.NotAnActualActivity
import com.geektechkb.core.typealiases.NotAnActualCallbacks
import com.geektechkb.core.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.core.typealiases.NotAnActualForceResendingToken
import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
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