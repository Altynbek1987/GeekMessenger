package com.geektechkb.feature_main.presentation.ui.fragments.profil.editProfile

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchUserUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserLastNameUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserNameUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserProfileImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val updateUserProfileImageUseCase: UpdateUserProfileImageUseCase,
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val updateUserLastNameUseCase: UpdateUserLastNameUseCase,
    private val fetchUserUseCase: FetchUserUseCase

) : BaseViewModel() {
    private val _userState = mutableUiStateFlow<User>()
    val userState = _userState.asStateFlow()

    fun fetchUser(phoneNumber: String) {
        viewModelScope.launch {
            fetchUserUseCase(phoneNumber).gatherRequest(_userState)
        }
    }


    suspend fun updateUserProfileImage(imageFileName: String, byte: ByteArray) =
        updateUserProfileImageUseCase(imageFileName, byte)

    fun updateUserName(name: String) = updateUserNameUseCase(name)

    fun updateUserLastName(lastName: String) = updateUserLastNameUseCase(lastName)


}