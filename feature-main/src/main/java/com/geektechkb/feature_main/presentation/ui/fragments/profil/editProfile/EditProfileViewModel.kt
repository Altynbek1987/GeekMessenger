package com.geektechkb.feature_main.presentation.ui.fragments.profil.editProfile

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateUserProfileImageUseCase: UpdateUserProfileImageUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateUserLastNameUseCase: UpdateUserLastNameUseCase,
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserProfileImageInFireStore: UpdateUserProfileImageInFireStore,
) : BaseViewModel() {
    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun fetchUser(phoneNumber: String) =
        fetchUserUseCase(phoneNumber).gatherRequest(_userState)

    suspend fun updateUserProfileImage(url: String) =
        updateUserProfileImageUseCase(url)

    suspend fun updateUserProfileImageInFireStore(url: String) {
            updateUserProfileImageInFireStore.invoke(url)
    }

    fun updateUserName(name: String) = updateUserNameUseCase(name)

    fun updateUserLastName(lastName: String) = updateUserLastNameUseCase(lastName)

}
