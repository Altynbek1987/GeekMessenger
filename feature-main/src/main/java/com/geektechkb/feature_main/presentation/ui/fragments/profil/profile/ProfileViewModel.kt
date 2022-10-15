package com.geektechkb.feature_main.presentation.ui.fragments.profil.profile

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserPhoneNumberHiddennessUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserProfileImageInFireStore
import com.geektechkb.feature_main.domain.useCases.UpdateUserProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserProfileImageUseCase: UpdateUserProfileImageUseCase,
    private val updateUserPhoneNumberHiddennessUseCase: UpdateUserPhoneNumberHiddennessUseCase,
    private val updateUserProfileImageInFireStore: UpdateUserProfileImageInFireStore
) : BaseViewModel() {
    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)

    suspend fun updateUserProfileImage(url: String) =
        updateUserProfileImageUseCase(url)

    fun hideUserPhoneNumber(isUserPhoneNumberHidden: Boolean) =
        updateUserPhoneNumberHiddennessUseCase(isUserPhoneNumberHidden)

    suspend fun updateUserProfileImageInFireStore(url: String) {
        updateUserProfileImageInFireStore.invoke(url)
    }
}