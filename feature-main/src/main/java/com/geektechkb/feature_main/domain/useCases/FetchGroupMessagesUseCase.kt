package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.GroupMessagesRepository
import javax.inject.Inject

class FetchGroupMessagesUseCase @Inject constructor(
	private val repository: GroupMessagesRepository
) {

	operator fun invoke(groupName: String) = repository.fetchGroupMessages(groupName)
}