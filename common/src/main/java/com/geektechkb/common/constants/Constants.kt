package com.geektechkb.common.constants

object Constants {
    const val IS_AUTHORIZED_KEY = "isAuthorized"
    const val VERIFICATION_ID_KEY = "verificationId"
    const val HAS_ONBOARD_BEEN_SHOWN_KEY = "hasOnBoardBeenShown"
    const val CURRENT_FIREBASE_USER_PHONE_NUMBER_KEY = "currentFirebaseUserPhoneNumber"
    const val FIREBASE_FIRESTORE_AUTHENTICATED_USERS_COLLECTION_PATH = "authenticatedUsers"
    const val FIREBASE_FIRESTORE_MESSAGES_COLLECTION_PATH = "personalMessages"
    const val FIREBASE_FIRESTORE_CHATTERS_KEY = "chatters"

    const val FIREBASE_USER_LAST_SEEN_TIME_KEY = "lastSeen"
    const val FIREBASE_USER_LATEST_MESSAGE_KEY = "latestMessage"
    const val FIREBASE_USER_PHONE_NUMBER_KEY = "phoneNumber"
    const val FIREBASE_USER_NAME_KEY = "name"

    const val FIREBASE_FIRESTORE_TIME_MESSAGE_WAS_SENT = "timeMessageWasSent"
    const val FIREBASE_USER_LAST_NAME_KEY = "lastName"
    const val FIREBASE_USER_PROFILE_IMAGE_KEY = "profileImage"
    const val FIREBASE_USER_MESSAGE_IMAGE_KEY = "messageImage"
    const val FIREBASE_USER_PHONE_NUMBER_HIDDENNESS = "isPhoneNumberHidden"
    const val YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT = "yyyy-MM-dd-HH:mm:ss"
    const val HOURS_MINUTES_DATE_FORMAT = "HH:mm"
    const val FIREBASE_CLOUD_STORAGE_PROFILE_IMAGES_PATH = "profileImages/"
    const val FIREBASE_CLOUD_STORAGE_MESSAGE_IMAGES_PATH = "messageImages/"
    const val FIREBASE_CLOUD_STORAGE_VOICE_MESSAGES_PATH = "voiceMessages/"

    const val INTENT_FILTER = "MESSENGER_EVENT"


    const val UNIQUE_HANDLER_ID = "uniqueHandlerId"
    const val CHANGE_LANGUAGE = "language"

    const val IS_PHONE_NUMBER_HIDDEN = "isPhoneNumberHidden"

    const val ARE_PRIVATE_CHATS_NOTIFICATIONS_TURNED_ON = "arePrivateChatsNotificationsTurnedOn"
    const val ARE_GROUP_CHATS_NOTIFICATIONS_TURNED_ON = "areGroupChatsNotificationsTurnedOn"
    const val ARE_CALLS_NOTIFICATIONS_TURNED_ON = "areCallsNotificationsTurnedOn"

    const val PRIVATE_CHATS_MESSAGE_TOPIC = "PrivateChats"
    const val GROUP_CHATS_MESSAGE_TOPIC = "GroupChats"
    const val CALLS_MESSAGE_TOPIC = "Calls"

    const val BASE_URL = "https://fcm.googleapis.com"
    const val SERVER_KEY =
        "AAAA4tl5Ows:APA91bE6G6MElF0snum44KolLtwwmnCecHS5gRCFNi5P7z4PpiL1SFEqBhjJoCqQ9UE9pz7hLAMH6UGSUQe-GdFcNy2lolWDOaLxO4LuVo9nTTCmGhu_FiXtuMBrdm1eW2D3giz-zrUs" // get firebase server key from firebase project setting
    const val CONTENT_TYPE = "application/json"
}