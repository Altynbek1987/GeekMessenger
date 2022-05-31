package com.geektechkb.feature_auth.presentation.ui.fragments.auth

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthWithPhoneNumberViewModel @Inject constructor(
    private val userPreferences: AuthorizePreferences,
) : BaseViewModel() {

    fun isAuthorizeTrue() {
        userPreferences.isAuthorized = true
    }
}