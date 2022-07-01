package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.core.typealiases.NotAnActualActivity
import com.geektechkb.core.typealiases.NotAnActualCallbacks
import com.geektechkb.core.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.core.typealiases.NotAnActualForceResendingToken

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
        lastSeen: String = "",
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String?,
        imageFileName: String
    )
}