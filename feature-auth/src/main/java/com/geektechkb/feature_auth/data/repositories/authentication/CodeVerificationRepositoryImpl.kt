package com.geektechkb.feature_auth.data.repositories.authentication

import androidx.appcompat.app.AppCompatActivity
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.core.typealiases.*
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.CodeVerificationRepository
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CodeVerificationRepositoryImpl @Inject constructor(
    private val authorizationPreferences: AuthorizePreferences
) : BaseRepository(), CodeVerificationRepository {
    override fun getVerificationId() = authorizationPreferences.verificationId


    override fun verifyPhoneNumberWithCode(
        verificationId: String,
        code: String,
    ) =
        PhoneAuthProvider.getCredential(verificationId, code)


    override fun resendVerificationCode(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualForceResendingToken: NotAnActualForceResendingToken?


    ) {
        val optionsBuilder = PhoneAuthOptions.newBuilder(notAnActualFirebaseAuth as FirebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(120L, TimeUnit.SECONDS)
            .setActivity(notAnActualActivity as AppCompatActivity)
            .setCallbacks(notAnActualCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks)

        notAnActualForceResendingToken?.let { optionsBuilder.setForceResendingToken(it as PhoneAuthProvider.ForceResendingToken) }

        PhoneAuthProvider.verifyPhoneNumber(optionsBuilder.build())
    }

    override fun signInWithPhoneAuthCredential(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        credential: NotAnActualPhoneAuthCredential,
        notAnActualActivity: NotAnActualActivity,
        userSuccessfullyVerifiedTheirPhoneNumber: ((() -> Unit))?,
        authenticationProcessFailed: ((() -> Unit
        ))?,
        ifUserHasEnteredInvalidCredentials: ((() -> Unit))?
    ) {
        notAnActualFirebaseAuth as FirebaseAuth
        notAnActualFirebaseAuth.signInWithCredential(credential as PhoneAuthCredential)
            .addOnCompleteListener(notAnActualActivity as AppCompatActivity) { task ->
                when (task.isSuccessful) {
                    true -> {
                        userSuccessfullyVerifiedTheirPhoneNumber?.invoke()
                        task.result.user
                    }
                    else -> authenticationProcessFailed?.invoke()
                }
                when (task.exception) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        ifUserHasEnteredInvalidCredentials?.invoke()


                    }
                }
            }
    }
}