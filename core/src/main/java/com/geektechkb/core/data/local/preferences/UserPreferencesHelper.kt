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


    var currentUserPhoneNumber: String
        get() = preferences.getString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, "").toString()
        set(value) = preferences.edit().putString(CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY, value)
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
}