package com.geektechkb.feature_auth.data.repositories.authentication

import androidx.appcompat.app.AppCompatActivity
import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualActivity
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualCallbacks
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualFirebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val authorizationPreferences: AuthorizePreferences,
    private val userRef: CollectionReference
) : BaseRepository(), AuthRepository {
    private var forceResendingToken: PhoneAuthProvider.ForceResendingToken? = null
    override fun isUserAuthenticated(): Boolean {
        authorizationPreferences.isAuthorized = firebaseAuth.currentUser != null
        return authorizationPreferences.isAuthorized
    }

    override fun provideAuthenticationCallbacks(
        authenticationSucceeded: ((() -> Unit))?,
        authInvalidCredentialsError: ((() -> Unit))?,
        tooManyRequestsError: ((() -> Unit))?
    ): PhoneAuthProvider.OnVerificationStateChangedCallbacks {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                authenticationSucceeded?.invoke()
            }

            override fun onVerificationFailed(e: FirebaseException) {

                if (e is FirebaseAuthInvalidCredentialsException) {
                    authInvalidCredentialsError?.invoke()
                } else if (e is FirebaseTooManyRequestsException) {
                    tooManyRequestsError?.invoke()
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

    override fun startPhoneNumberVerification(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks
    ) {
        notAnActualFirebaseAuth as FirebaseAuth
        notAnActualFirebaseAuth.setLanguageCode("ru")
        val options = PhoneAuthOptions.newBuilder(notAnActualFirebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(notAnActualActivity as AppCompatActivity)
            .setCallbacks(
                notAnActualCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks
            )
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    override fun provideResendingToken() = forceResendingToken
    override suspend fun authenticateUser(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String
    ) {
        when (profileImage) {
            profileImage -> addDocument(
                userRef,
                hashMapOf(
                    "phoneNumber" to phoneNumber,
                    "name" to name,
                    "surname" to surname,
                    "profileImage" to profileImage
                ),
                phoneNumber
            )
            else -> addDocument(
                userRef,
                hashMapOf(
                    "phoneNumber" to phoneNumber,
                    "name" to name,
                    "surname" to surname,
                    "" to profileImage
                ),
                phoneNumber
            )
        }
    }
}