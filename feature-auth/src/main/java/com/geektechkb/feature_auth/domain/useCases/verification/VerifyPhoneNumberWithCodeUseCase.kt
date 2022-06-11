package com.geektechkb.feature_auth.domain.useCases.verification

import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import javax.inject.Inject

class VerifyPhoneNumberWithCodeUseCase @Inject constructor(
    private val codeVerificationRepository: CodeVerificationRepository
) {
    operator fun invoke(verificationId: String, code: String) =
        codeVerificationRepository.verifyPhoneNumberWithCode(verificationId, code)
}