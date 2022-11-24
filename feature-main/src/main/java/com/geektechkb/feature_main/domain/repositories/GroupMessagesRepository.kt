package com.geektechkb.feature_main.domain.repositories

import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.geektechkb.common.either.Either
import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.core.typealiases.NotAnActualPaginator
import com.geektechkb.feature_main.domain.models.Group
import com.geektechkb.feature_main.domain.models.GroupMessage
import com.geektechkb.feature_main.domain.models.User
import kotlinx.coroutines.flow.Flow

interface GroupMessagesRepository {

    suspend fun addUserToGroup(
        groupName: String,
        userList: List<User>,
        groupPhoto: String,
        userCount: Int,
        userNumber: String,
    )

//    suspend fun senMediaToGroup(
//        media: String?,
//        mediaType: String?,
//        mediaResource: String,
//    )

    suspend fun sendMessageToGroup(
        groupName: String,
        senderPhoneNumber: String,
        receiversPhoneNumbers: List<String>,
        message: String,
        media: String?,
        mediaType: String,
        timeMessageWasSent: String,
        messageId: String,
    )

//    fun fetchMediaToGroup(media: String): Flow<List<GroupMessage?>>

    fun fetchGroupInfo(groupName: String): Flow<Either<String, Group>>

    fun fetchGroups(): Flow<Either<String, List<Group>>>

    fun fetchGroupMessages(groupName: String): Flow<List<GroupMessage?>>

    fun createGroupPaginator(notAnActualHitsSearcher: NotAnActualHitsSearcher): NotAnActualPaginator

//    fun fetchPagedMessages(
//        senderPhoneNumber: String,
//        receiverPhoneNumber: String
//    ): Flow<List<Message?>>


    fun createHitsSearcher(
        applicationId: String,
        apiKey: String,
        indexName: String,
        query: String
    ): HitsSearcher
}