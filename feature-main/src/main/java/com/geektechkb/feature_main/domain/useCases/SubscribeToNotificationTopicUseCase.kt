package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class SubscribeToNotificationTopicUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
	operator fun invoke(topics: String) =
		usersRepository.subscribeToNotificationTopic(topics)
}