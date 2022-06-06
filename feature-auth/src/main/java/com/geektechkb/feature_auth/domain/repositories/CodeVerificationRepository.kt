package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.feature_auth.domain.typealiases.*

interface CodeVerificationRepository {

    fun getVerificationId(): String
    fun verifyPhoneNumberWithCode(
        verificationId: String,
        code: String,
    ): NotAnActualPhoneAuthCredential

    fun resendVerificationCode(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualForceResendingToken: NotAnActualForceResendingToken?
    )

    fun signInWithPhoneAuthCredential(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        credential: NotAnActualPhoneAuthCredential,
        notAnActualActivity: NotAnActualActivity,
        userSuccessfullyVerifiedTheirPhoneNumber: ((() -> Unit))?,
        authenticationProcessFailed: ((() -> Unit))?,
        ifUserHasEnteredInvalidCredentials: ((() -> Unit))?
    )

}