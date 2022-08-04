package com.geektechkb.feature_auth.presentation.ui.fragments.auth.signUp

import androidx.appcompat.app.AppCompatActivity
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.core.typealiases.NotAnActualActivity
import com.geektechkb.core.typealiases.NotAnActualCallbacks
import com.geektechkb.core.typealiases.NotAnActualFirebaseAuth
import com.geektechkb.feature_auth.domain.useCases.authentication.ProvideAuthenticationCallbacksUseCase
import com.geektechkb.feature_auth.domain.useCases.authentication.StartPhoneNumberVerificationUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    val firebaseAuth: FirebaseAuth,
    private val provideAuthenticationCallbacksUseCase: ProvideAuthenticationCallbacksUseCase,
    private val startPhoneNumberVerificationUseCase: StartPhoneNumberVerificationUseCase
) : BaseViewModel() {


    fun provideCallbacks(
        authenticationSucceeded: ((() -> Unit))?,
        authInvalidCredentialsError: ((() -> Unit))?,
        tooManyRequestsError: ((() -> Unit))?
    ) = provideAuthenticationCallbacksUseCase(authenticationSucceeded = {
        authenticationSucceeded?.invoke()
    }, authInvalidCredentialsError = {
        authInvalidCredentialsError?.invoke()
    }, tooManyRequestsError = {
        tooManyRequestsError?.invoke()
    }

    ) as PhoneAuthProvider.OnVerificationStateChangedCallbacks

    fun startPhoneNumberVerification(
        notAnActualFirebaseAuth: NotAnActualFirebaseAuth,
        phoneNumber: String,
        notAnActualActivity: NotAnActualActivity,
        notAnActualCallbacks: NotAnActualCallbacks,
    ) {
        startPhoneNumberVerificationUseCase(
            notAnActualFirebaseAuth as FirebaseAuth,
            phoneNumber,
            notAnActualActivity as AppCompatActivity,
            notAnActualCallbacks as PhoneAuthProvider.OnVerificationStateChangedCallbacks
        )
        notAnActualFirebaseAuth.setLanguageCode("ru-RU")
    }
}