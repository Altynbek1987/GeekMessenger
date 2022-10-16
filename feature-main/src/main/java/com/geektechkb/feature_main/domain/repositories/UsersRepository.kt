package com.geektechkb.feature_main.domain.repositories

import com.geektechkb.common.either.Either
import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.core.typealiases.NotAnActualPaginator
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {

    fun fetchUser(phoneNumber: String): Flow<Either<String, User>>

    fun updateUserStatus(status: String)

    suspend fun updateUserProfileImage(url: String): String

    fun updateUserName(name: String)

    fun updateUserLastName(lastName: String)

    suspend fun updateUserProfileImageInFireStore(url: String)

    fun updateUserNumberHiddenness(isUserPhoneNumberHidden: Boolean)

    fun subscribeToNotificationTopic(vararg topics: String)

    fun unsubscribeFromNotificationTopic(vararg topics: String)

    fun createHitsSearcher(
        applicationId: String,
        apiKey: String,
        indexName: String
    ): NotAnActualHitsSearcher

    fun createPaginator(notAnActualHitsSearcher: NotAnActualHitsSearcher): NotAnActualPaginator

    fun getCurrentUserPhoneNumber(): String?


}