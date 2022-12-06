package com.geektechkb.feature_main.presentation.ui.fragments.profil.notificationsAndSounds

import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.useCases.SubscribeToNotificationTopicUseCase
import com.geektechkb.feature_main.domain.useCases.UnsubscribeFromNotificationTopicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationsAndSoundsViewModel @Inject constructor(
    private val subscribeToNotificationTopicUseCase: SubscribeToNotificationTopicUseCase,
    private val unsubscribeFromNotificationTopicUseCase: UnsubscribeFromNotificationTopicUseCase
) : BaseViewModel() {
	fun subscribeToNotificationTopic(topic: String) =
		subscribeToNotificationTopicUseCase(topic)

	fun unsubscribeFromNotificationTopic(topic: String) =
		unsubscribeFromNotificationTopicUseCase(topic)
}