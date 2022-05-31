package com.geektechkb.feature_auth.data.repositories.authentication

import com.geektechkb.core.base.BaseRepository
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.domain.repositories.AuthRepository
import com.geektechkb.feature_auth.utils.NotAnActualActivity
import com.geektechkb.feature_auth.utils.NotAnActualCallbacks
import com.geektechkb.feature_auth.utils.NotAnActualFirebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit
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
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth

    ) {
        notAnActualFirebaseAuth.setLanguageCode("ru")
        val options = PhoneAuthOptions.newBuilder(notAnActualFirebaseAuth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(notAnActualActivity)
            .setCallbacks(
                provideAuthenticationCallbacks(
                )
            )
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }
    //    fun cast(){
//        val nonAnActualActivity = NonAnActualActivity ()
//        nonAnActualActivity as AppCompatActivity
//    }

    fun provideResendingToken() = forceResendingToken
}