package com.geektechkb.feature_auth.domain.useCases.verification

import com.geektechkb.feature_auth.domain.useCases.authentication.verification.GetVerificationIdUseCase

data class VerificationUseCases(
    val getVerificationIdUseCase: GetVerificationIdUseCase,
    val resendVerificationCodeUseCase: ResendVerificationCodeUseCase,
    val signInWithPhoneAuthCredentialUseCase: SignInWithPhoneAuthCredentialUseCase,
    val verifyPhoneNumberWithCodeUseCase: VerifyPhoneNumberWithCodeUseCase
)