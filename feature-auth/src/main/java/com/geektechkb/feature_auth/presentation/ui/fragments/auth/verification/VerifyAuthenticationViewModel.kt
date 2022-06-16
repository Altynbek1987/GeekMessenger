package com.geektechkb.feature_auth.presentation.ui.fragments.auth.verification

import androidx.appcompat.app.AppCompatActivity
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.core.typealiases.*
import com.geektechkb.feature_auth.domain.useCases.authentication.ProvideAuthenticationCallbacksUseCase
import com.geektechkb.feature_auth.domain.useCases.authentication.ProvideForceResendingTokenUseCase
import com.geektechkb.feature_auth.domain.useCases.verification.GetVerificationIdUseCase
import com.geektechkb.feature_auth.domain.useCases.verification.ResendVerificationCodeUseCase
import com.geektechkb.feature_auth.domain.useCases.verification.SignInWithPhoneAuthCredentialUseCase
import com.geektechkb.feature_auth.domain.useCases.verification.VerifyPhoneNumberWithCodeUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyAuthenticationViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val verifyPhoneNumberWithCodeUseCase: VerifyPhoneNumberWithCodeUseCase,
    private val resendVerificationCodeUseCase: ResendVerificationCodeUseCase,
    private val provideAuthenticationCallbacksUseCase: ProvideAuthenticationCallbacksUseCase,
    private val signInWithPhoneAuthCredentialUseCase: SignInWithPhoneAuthCredentialUseCase,
    private val getVerificationIdUseCase: GetVerificationIdUseCase,
    private val provideForceResendingTokenUseCase: ProvideForceResendingTokenUseCase,
) : BaseViewModel() {
    fun verifyPhoneNumberWithCode(
        verificationId: String,
        code: String,
    ) = verifyPhoneNumberWithCodeUseCase(
        verificationId,
        code
    ) as PhoneAuthCredential


    fun provideCallbacks(
        authenticationSucceeded: ((() -> Unit))?,
        authInvalidCredentialsError: ((() -> Unit))?,
        tooManyRequestsError: ((() -> Unit))?
    ) = provideAuthenticationCallbacksUseCase(authenticationSucceeded = {
        authenticationSucceeded?.invoke()
    }, authInvalidCredentialsError = {
        authInvalidCredentialsError?.invoke()
    }, tooManyRequestsError = {
        tooManyRequestsError?.invoke()
    }

    )

    fun resendVerificationCode(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualForceResendingToken: NotAnActualForceResendingToken?
    ) = resendVerificationCodeUseCase(
        notAnActualFirebaseAuth as FirebaseAuth,
        phoneNumber,
        notAnActualActivity as AppCompatActivity,
        notAnActualCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        notAnActualForceResendingToken as PhoneAuthProvider.ForceResendingToken?
    )

    fun signInWithPhoneAuthCredential(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        credential: NotAnActualPhoneAuthCredential,
        notAnActualActivity: NotAnActualActivity,
        userSuccessfullyVerifiedTheirPhoneNumber: ((() -> Unit))?,
        authenticationProcessFailed: ((() -> Unit))?,
        ifUserHasEnteredInvalidCredentials: ((() -> Unit))?
    ) = signInWithPhoneAuthCredentialUseCase(
        notAnActualFirebaseAuth as FirebaseAuth,
        credential as PhoneAuthCredential, notAnActualActivity as AppCompatActivity,
        userSuccessfullyVerifiedTheirPhoneNumber = {
            userSuccessfullyVerifiedTheirPhoneNumber?.invoke()
        }, authenticationProcessFailed = {
            authenticationProcessFailed?.invoke()
        }, ifUserHasEnteredInvalidCredentials = {
            ifUserHasEnteredInvalidCredentials?.invoke()
        }
    )

    fun getVerificationId() = getVerificationIdUseCase()
    fun getForceResendingToken() =
        provideForceResendingTokenUseCase() as PhoneAuthProvider.ForceResendingToken?

}