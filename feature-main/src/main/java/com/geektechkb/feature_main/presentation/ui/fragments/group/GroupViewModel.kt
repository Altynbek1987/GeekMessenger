package com.geektechkb.feature_main.presentation.ui.fragments.group

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.domain.useCases.CreateHitsSearcherUseCase
import com.geektechkb.feature_main.domain.useCases.CreatePaginatorUseCase
import com.geektechkb.feature_main.domain.useCases.GetCurrentUserPhoneNumberUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(
    createHitsSearcherUseCase: CreateHitsSearcherUseCase,
    createPaginatorUseCase: CreatePaginatorUseCase,
    private val getCurrentUserPhoneNumberUseCase: GetCurrentUserPhoneNumberUseCase,
) : BaseViewModel() {

    fun getCurrentUserPhoneNumber() = getCurrentUserPhoneNumberUseCase()

    private val searcher =
        createHitsSearcherUseCase(APPLICATION_ID, API_KEY, ALGOLIA_INDEX_NAME) as HitsSearcher
    val paginator = createPaginatorUseCase(searcher) as Paginator<User>

    val searchBox = SearchBoxConnector(searcher)
    private val connectionHandler = ConnectionHandler(searchBox)

    init {
        connectionHandler += searchBox.connectPaginator(paginator)
    }

    override fun onCleared() {
        super.onCleared()
        searcher.cancel()
        connectionHandler.clear()
    }

    companion object {
        private const val APPLICATION_ID = "3YM4HY2EQZ"
        private const val API_KEY = "2b198fbbea989902b88cf07a8236202c"
        private const val ALGOLIA_INDEX_NAME = "authenticatedUsers"
    }
}