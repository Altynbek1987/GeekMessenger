package com.geektechkb.feature_main.presentation.ui.fragments.settings

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.UpdateUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainFlowViewModel @Inject constructor(private val updateUserStatusUseCase: UpdateUserStatusUseCase) :
    BaseViewModel() {
    fun updateUserStatus(status: String) = updateUserStatusUseCase(status)
}