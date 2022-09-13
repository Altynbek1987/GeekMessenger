package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_auth.domain.useCases.authentication.AuthenticateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
) : BaseViewModel() {

    private val _authenticationState = mutableUiStateFlow<Unit>()
    val authenticationState = _authenticationState.asStateFlow()

    fun authenticateUser(
        lastSeen: String,
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String?,
        imageFileName: String,
    ) = authenticateUserUseCase(
        lastSeen,
        phoneNumber,
        name,
        surname,
        profileImage,
        imageFileName,
    ).gatherRequest(_authenticationState)
}