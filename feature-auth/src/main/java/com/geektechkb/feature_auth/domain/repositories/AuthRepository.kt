package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.feature_auth.utils.NotAnActualActivity
import com.geektechkb.feature_auth.utils.NotAnActualCallbacks
import com.geektechkb.feature_auth.utils.NotAnActualFirebaseAuth

interface AuthRepository {

    fun isUserAuthenticated(): Boolean
    fun getSmsCode() : String
    fun provideAuthenticationCallbacks(
        authenticationSucceed: ((() -> Unit))? = null,
        authInvalidCredentialsError: ((() -> Unit))? = null,
        tooManyRequestsError: ((() -> Unit))? = null,

        ): NotAnActualCallbacks

    fun startPhoneNumberVerification(
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth
    )
}