package com.geektechkb.feature_main.presentation.ui.fragments.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedUsersUseCase
import com.geektechkb.feature_main.domain.useCases.UpdateUserStatusUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchPagedUsersUseCase: FetchPagedUsersUseCase,
    private val updateUserStatusUseCase: UpdateUserStatusUseCase
) : BaseViewModel() {
    fun fetchPagedUsers(): Flow<PagingData<User>> {
        val pagedUsers = fetchPagedUsersUseCase() as Flow<PagingData<User>>
        return pagedUsers.cachedIn(viewModelScope)

    }

    fun updateUserStatus(status: String) {
        viewModelScope.launch {
            updateUserStatusUseCase(status)
        }
    }

    init {
        fetchPagedUsers()

    }


}