package com.geektechkb.feature_auth.domain.repositories

import com.geektechkb.feature_auth.utils.NotAnActualActivity
import com.geektechkb.feature_auth.utils.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.utils.NotAnActualPhoneAuthProviderCallbacks

interface AuthRepository {

    fun isUserAuthenticated(): Boolean
    fun startPhoneNumberVerification(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualPhoneAuthProviderCallbacks: NotAnActualPhoneAuthProviderCallbacks
    )
}