package com.geektechkb.feature_main.presentation.ui.fragments.group.create

import androidx.lifecycle.viewModelScope
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.AddUserToGroupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGroupViewModel @Inject constructor(
	private val addUserToGroupUseCase: AddUserToGroupUseCase
) : BaseViewModel() {

	fun addUserToGroup(
		groupName: String,
		userList: List<User>,
		groupPhoto: String,
		userCount: Int,
		userNumber: String
	) {
		viewModelScope.launch {
			addUserToGroupUseCase(groupName, userList, groupPhoto, userCount, userNumber)
		}
	}
}