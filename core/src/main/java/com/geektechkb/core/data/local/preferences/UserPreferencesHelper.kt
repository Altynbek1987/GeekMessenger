package com.geektechkb.core.data.local.preferences

import android.content.SharedPreferences
import com.geektechkb.common.constants.Constants.ARE_CALLS_NOTIFICATIONS_TURNED_ON
import com.geektechkb.common.constants.Constants.ARE_GROUP_CHATS_NOTIFICATIONS_TURNED_ON
import com.geektechkb.common.constants.Constants.ARE_PRIVATE_CHATS_NOTIFICATIONS_TURNED_ON
import com.geektechkb.common.constants.Constants.CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY
import com.geektechkb.common.constants.Constants.IS_PHONE_NUMBER_HIDDEN
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesHelper @Inject constructor(private val preferences: SharedPreferences) {

	private fun remove(key: String) {
		preferences.edit().remove(key).apply()
	}

	var currentUserPhoneNumber: String
		get() = preferences.getString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, "").toString()
		set(value) = preferences.edit().putString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, value)
			.apply()
	var currentUserName: String
		get() = preferences.getString("userName", "").toString()
		set(value) = preferences.edit().putString("userName", value)
			.apply()

	var isPhoneNumberHidden: Boolean
		get() = preferences.getBoolean(IS_PHONE_NUMBER_HIDDEN, false)
		set(value) = preferences.edit().putBoolean(IS_PHONE_NUMBER_HIDDEN, value).apply()

	var arePrivateChatsNotificationsTurnedOn: Boolean
		get() = preferences.getBoolean(ARE_PRIVATE_CHATS_NOTIFICATIONS_TURNED_ON, true)
		set(value) = preferences.edit().putBoolean(ARE_PRIVATE_CHATS_NOTIFICATIONS_TURNED_ON, value)
			.apply()
	var areGroupChatsNotificationsTurnedOn: Boolean
		get() = preferences.getBoolean(ARE_GROUP_CHATS_NOTIFICATIONS_TURNED_ON, true)
		set(value) = preferences.edit().putBoolean(ARE_GROUP_CHATS_NOTIFICATIONS_TURNED_ON, value)
			.apply()

	var areCallsNotificationsTurnedOn: Boolean
		get() = preferences.getBoolean(ARE_CALLS_NOTIFICATIONS_TURNED_ON, true)
		set(value) = preferences.edit().putBoolean(ARE_CALLS_NOTIFICATIONS_TURNED_ON, value).apply()

	var notificationsToken
		get() = preferences.getString("notifications_token", "").toString()
		set(value) = preferences.edit().putString(
			"notifications_token", value
		).apply()

	var isNotificationsEnabled
		get() = preferences.getBoolean("notifications_is_enabled", false)
		set(value) = preferences.edit().putBoolean(
			"notifications_is_enabled", value
		).apply()

	fun removeNotificationsToken() {
		remove("notifications_token")
	}
}