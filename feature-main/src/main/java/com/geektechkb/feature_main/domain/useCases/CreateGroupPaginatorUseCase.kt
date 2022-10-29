package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.feature_main.domain.repositories.GroupMessagesRepository
import javax.inject.Inject

class CreateGroupPaginatorUseCase @Inject constructor(
    private val usersGroupRepository: GroupMessagesRepository
) {
    operator fun invoke(notAnActualHitsSearcher: NotAnActualHitsSearcher) =
        usersGroupRepository.createGroupPaginator(notAnActualHitsSearcher)
}