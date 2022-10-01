package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class UnsubscribeFromNotificationTopicUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(vararg topics: String) =
        usersRepository.unsubscribeFromNotificationTopic(*topics)
}