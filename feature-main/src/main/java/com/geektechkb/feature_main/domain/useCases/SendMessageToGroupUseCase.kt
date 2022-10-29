package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.GroupMessagesRepository
import javax.inject.Inject

class SendMessageToGroupUseCase @Inject constructor(
	private val repository: GroupMessagesRepository
) {

	suspend operator fun invoke(
		groupName: String,
		senderPhoneNumber: String,
		receiversPhoneNumbers: List<String>,
		message: String,
		timeMessageWasSent: String,
		messageId: String,
	) = repository.sendMessageToGroup(
		groupName,
		senderPhoneNumber,
		receiversPhoneNumbers,
		message,
		timeMessageWasSent,
		messageId,


	)
}