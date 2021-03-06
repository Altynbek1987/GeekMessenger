package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_auth.domain.useCases.authentication.AuthenticateUserUseCase
import com.geektechkb.feature_auth.domain.useCases.authentication.IsUserAuthenticatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase
) : BaseViewModel() {
    suspend fun authenticateUser(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String = " "
    ) =
        authenticateUserUseCase(phoneNumber, name, surname, profileImage)
    fun isUserAuthenticated() = isUserAuthenticatedUseCase()
}