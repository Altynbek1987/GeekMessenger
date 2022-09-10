package com.geektechkb.feature_main.presentation

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserProfileImageUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainFlowViewModel @Inject constructor(
    private val updateUserStatusUseCase: UpdateUserStatusUseCase,
    private val fetchUserUseCase: FetchUserUseCase,

) :
    BaseViewModel() {
    fun updateUserStatus(status: String) = updateUserStatusUseCase(status)

    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)
}