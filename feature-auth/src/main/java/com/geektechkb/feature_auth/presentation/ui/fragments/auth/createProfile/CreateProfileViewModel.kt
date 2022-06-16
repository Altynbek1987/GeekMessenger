package com.geektechkb.feature_auth.presentation.ui.fragments.auth.createProfile

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_auth.domain.useCases.authentication.AuthenticateUserUseCase
import com.geektechkb.feature_auth.domain.useCases.authentication.IsUserAuthenticatedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProfileViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
    private val uploadImageToCloudStorageUseCase: UploadImageToCloudStorageUseCase
) : BaseViewModel() {
    suspend fun authenticateUser(
        phoneNumber: String,
        name: String,
        surname: String,
        profileImage: String = " "
    ) =
        authenticateUserUseCase(phoneNumber, name, surname, profileImage)

    fun isUserAuthenticated() = isUserAuthenticatedUseCase()
    fun uploadImageToCloudStorage(image: ByteArray?, id: String) {
        viewModelScope.launch {
            uploadImageToCloudStorageUseCase(image, id)
        }
    }
}