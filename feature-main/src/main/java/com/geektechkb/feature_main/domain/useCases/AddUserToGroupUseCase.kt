package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.repositories.GroupMessagesRepository
import javax.inject.Inject

class AddUserToGroupUseCase @Inject constructor(
	private val repository: GroupMessagesRepository
) {

	suspend operator fun invoke(
		groupName: String,
		userList: List<User>,
		groupPhoto: String,
		userCount: Int,
		userNumber: String
	) =
		repository.addUserToGroup(groupName, userList, groupPhoto, userCount, userNumber)
}