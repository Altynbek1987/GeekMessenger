package com.geektechkb.feature_main.presentation.ui.fragments.home

import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.data.repositories.UsersRepositoryImpl
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.FetchPagedUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchUsersUseCase: FetchPagedUseCase,
    private val usersRepositoryImpl: UsersRepositoryImpl
) : BaseViewModel() {
    private val _fetch  = mutableUiStateFlow<List<User>>()
    val fetch=  _fetch.asStateFlow()
    fun fetchPagedUsers() = usersRepositoryImpl.fetchPagedUsersr().cachedIn(viewModelScope)

    init {
        fetch()
    }
    fun fetch() {
        usersRepositoryImpl.fetchIng().gatherRequest(_fetch)
    }
}