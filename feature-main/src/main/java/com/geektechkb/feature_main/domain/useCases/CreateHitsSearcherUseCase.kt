package com.geektechkb.feature_main.domain.useCases

import com.geektechkb.feature_main.domain.repositories.UsersRepository
import javax.inject.Inject

class CreateHitsSearcherUseCase @Inject constructor(private val usersRepository: UsersRepository) {
    operator fun invoke(applicationId: String, apiKey: String, indexName: String) =
        usersRepository.createHitsSearcher(applicationId, apiKey, indexName)
}