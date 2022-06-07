package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.feature_auth.domain.typealiases.NotAnActualActivity
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualCallbacks
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.domain.typealiases.NotAnActualForceResendingToken

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
    suspend fun authenticateUser(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String
    )
}