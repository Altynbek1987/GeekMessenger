package com.geektechkb.feature_auth.data.repositories.authentication

import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authorizationPreferences: AuthorizePreferences,
) : BaseRepository(), AuthRepository {
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    override fun isUserAuthenticated(): Boolean {
        authorizationPreferences.isAuthorized = firebaseAuth.currentUser != null
        return authorizationPreferences.isAuthorized
    }

    fun provideAuthCallback(
        authenticationSucceed: () -> Unit,
        authInvalidCredentialsError: () -> Unit,
        tooManyRequestsError: () -> Unit
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                authenticationSucceed()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    authInvalidCredentialsError()
                } else if (e is FirebaseTooManyRequestsException) {
                    tooManyRequestsError()
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {

                authorizationPreferences.verificationId = verificationId
                forceResendingToken = token
            }

        }
        return callbacks
    }

    fun provideResendingToken() = forceResendingToken
}