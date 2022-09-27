package com.geektechkb.feature_main.presentation.ui.fragments.home

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
class HomeViewModel @Inject constructor(
    createHitsSearcherUseCase: CreateHitsSearcherUseCase,
    createPaginatorUseCase: CreatePaginatorUseCase,
    private val getCurrentUserPhoneNumberUseCase: GetCurrentUserPhoneNumberUseCase,
) : BaseViewModel() {

    fun getCurrentUserPhoneNumber() = getCurrentUserPhoneNumberUseCase()

    private val searcher = createHitsSearcherUseCase(
        APPLICATION_ID,
        API_KEY,
        ALGOLIA_INDEX_NAME
    ) as HitsSearcher
    val paginator =
        createPaginatorUseCase(searcher) as Paginator<User>

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
        private const val APPLICATION_ID = "D3DVWWYZ3S"
        private const val API_KEY = "c7245757ed76948cc6a464e3d669d384"
        private const val ALGOLIA_INDEX_NAME = "authenticatedUsers"
    }
}