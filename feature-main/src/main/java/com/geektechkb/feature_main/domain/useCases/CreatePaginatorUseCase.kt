package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.core.typealiases.NotAnActualHitsSearcher
import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class CreatePaginatorUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(notAnActualHitsSearcher: NotAnActualHitsSearcher) =
        usersRepository.createPaginator(notAnActualHitsSearcher)
}