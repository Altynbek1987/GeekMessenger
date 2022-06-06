package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.feature_auth.utils.NotAnActualActivity
import com.geektechkb.feature_auth.utils.NotAnActualCallbacks
import com.geektechkb.feature_auth.utils.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.utils.NotAnActualForceResendingToken

interface AuthRepository {

    fun isUserAuthenticated(): Boolean
    fun provideAuthenticationCallbacks(
        authenticationSucceeded: ((() -> Unit))? = null,
        authInvalidCredentialsError: ((() -> Unit))? = null,
        tooManyRequestsError: ((() -> Unit))? = null,

        ): NotAnActualCallbacks

    fun startPhoneNumberVerification(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
    )


    fun provideResendingToken(): NotAnActualForceResendingToken?
}
