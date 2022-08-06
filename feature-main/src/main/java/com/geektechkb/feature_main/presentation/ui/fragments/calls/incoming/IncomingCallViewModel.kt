package com.geektechkb.feature_main.presentation.ui.fragments.calls.incoming

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.AcceptAnIncomingCallUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IncomingCallViewModel @Inject constructor(private val acceptAnIncomingCallUseCase: AcceptAnIncomingCallUseCase) :
    BaseViewModel() {
    fun acceptAnIncomingCall() = acceptAnIncomingCallUseCase()
}