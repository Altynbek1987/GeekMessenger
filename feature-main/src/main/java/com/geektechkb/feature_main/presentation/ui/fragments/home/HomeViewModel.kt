package com.geektechkb.feature_main.presentation.ui.fragments.home

import androidx.paging.PagingData
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val fetchUsersUseCase: FetchPagedUseCase) :
    BaseViewModel() {
    fun fetchPagedUsers() = fetchUsersUseCase() as Flow<PagingData<User>>

}