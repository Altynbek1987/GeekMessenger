package com.geektechkb.feature_main.presentation.ui.fragments.profil.notificationsAndSounds

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants.CALLS_MESSAGE_TOPIC
import com.geektechkb.common.constants.Constants.GROUP_CHATS_MESSAGE_TOPIC
import com.geektechkb.common.constants.Constants.PRIVATE_CHATS_MESSAGE_TOPIC
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.actionOnCheckedChange
import com.geektechkb.core.extensions.setOnSingleClickListener
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentNotificationsAndSoundsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class NotificationsAndSoundsFragment :
    BaseFragment<FragmentNotificationsAndSoundsBinding, NotificationsAndSoundsViewModel>(R.layout.fragment_notifications_and_sounds) {
    override val binding by viewBinding(FragmentNotificationsAndSoundsBinding::bind)
    override val viewModel by viewModels<NotificationsAndSoundsViewModel>()

    @Inject
    lateinit var preferencesHelper: UserPreferencesHelper

    override fun initialize() {
        manageAllNotificationsSubscriptions()
    }

    private fun manageAllNotificationsSubscriptions() = with(preferencesHelper) {
        manageNotificationsSubscription(
            arePrivateChatsNotificationsTurnedOn,
            PRIVATE_CHATS_MESSAGE_TOPIC
        )
        manageNotificationsSubscription(
            areGroupChatsNotificationsTurnedOn,
            GROUP_CHATS_MESSAGE_TOPIC
        )
        manageNotificationsSubscription(
            areCallsNotificationsTurnedOn,
            CALLS_MESSAGE_TOPIC
        )
    }


    override fun assembleViews() {
        checkSwitchesIfAccordingNotificationIsTurnedOn()
    }

    private fun checkSwitchesIfAccordingNotificationIsTurnedOn() = with(preferencesHelper) {
        binding.apply {
            switchPrivateChatsNotifications.isChecked =
                arePrivateChatsNotificationsTurnedOn == true
            switchGroupChatsNotifications.isChecked =
                areGroupChatsNotificationsTurnedOn
            switchCallsNotifications.isChecked =
                areCallsNotificationsTurnedOn == true
        }
    }

    override fun setupListeners() {
        manipulateWithNotifications()

        binding.imBack.setOnSingleClickListener {
            findNavController().navigateUp()
        }
    }

    private fun manipulateWithNotifications() = with(preferencesHelper) {
        binding.apply {
            switchPrivateChatsNotifications.actionOnCheckedChange {
                arePrivateChatsNotificationsTurnedOn = it
            }

            switchGroupChatsNotifications.actionOnCheckedChange {
                areGroupChatsNotificationsTurnedOn = it
            }

            switchCallsNotifications.actionOnCheckedChange {
                areCallsNotificationsTurnedOn = it
            }
        }
    }

    private fun manageNotificationsSubscription(shouldSubscribeOrNot: Boolean, topic: String) {
        when (shouldSubscribeOrNot) {
            true -> viewModel.subscribeToNotificationTopic(topic)
            false -> viewModel.unsubscribeFromNotificationTopic(topic)
        }
    }
}