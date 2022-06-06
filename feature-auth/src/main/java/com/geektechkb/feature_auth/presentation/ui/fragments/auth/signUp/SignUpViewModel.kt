package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import android.content.Context
import android.widget.Toast
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_auth.data.repositories.authentication.AuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val authRepositoryImpl: AuthRepositoryImpl,
    private val isUserAuthenticatedUseCase: IsUserAuthenticatedUseCase,
) : BaseViewModel() {
    fun setRussianLanguageCode() {
        firebaseAuth.setLanguageCode("ru")
    }

    fun signUp() = isUserAuthenticatedUseCase()
    fun provideCallback(
        context: Context
    ) = authRepositoryImpl.provideAuthCallback(
        authenticationSucceed = {
            Toast.makeText(context, "You have requested verification code", Toast.LENGTH_SHORT)
                .show()
        },
        authInvalidCredentialsError = {
            Toast.makeText(context, "You have entered the wrong number", Toast.LENGTH_SHORT).show()

        },
        tooManyRequestsError = {

            Toast.makeText(
                context,
                "You have exceeded verification code request limit",
                Toast.LENGTH_SHORT
            ).show()
        }
    )
}