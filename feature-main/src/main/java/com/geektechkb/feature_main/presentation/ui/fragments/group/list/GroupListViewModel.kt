package com.geektechkb.feature_main.presentation.ui.fragments.group.list

import com.algolia.instantsearch.android.paging3.Paginator
import com.algolia.instantsearch.android.paging3.searchbox.connectPaginator
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.SearchBoxConnector
import com.algolia.instantsearch.searcher.hits.HitsSearcher
import com.geektechkb.core.base.BaseViewModel
import com.geektechkb.feature_main.domain.models.Group
import com.geektechkb.feature_main.domain.useCases.CreateGroupPaginatorUseCase
import com.geektechkb.feature_main.domain.useCases.CreateHitsSearcherUseCase
import com.geektechkb.feature_main.domain.useCases.FetchGroupsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GroupListViewModel @Inject constructor(
	createHitsSearcherUseCase: CreateHitsSearcherUseCase,
	createGroupPaginatorUseCase: CreateGroupPaginatorUseCase,
	private val fetchGroupsUseCase: FetchGroupsUseCase
) : BaseViewModel() {

	private val _groupsState = mutableUiStateFlow<List<Group>>()
	val groupsState = _groupsState.asStateFlow()

//	private val searcher = createHitsSearcherUseCase(
//		APPLICATION_ID,
//		API_KEY,
//		ALGOLIA_INDEX_NAME_GROUPS
//	) as HitsSearcher
//	val groupPaginator = createGroupPaginatorUseCase(searcher) as Paginator<Group>
//
//	val searchBox = SearchBoxConnector(searcher)
//	private val connectionHandler = ConnectionHandler(searchBox)

	private fun fetchGroups() {
		fetchGroupsUseCase().gatherRequest(_groupsState)
	}

	init {
		fetchGroups()
	}

//	connectionHandler += searchBox.connectPaginator(groupPaginator)
//	override fun onCleared() {
//		super.onCleared()
//		searcher.cancel()
//		connectionHandler.clear()
//	}
//
//	companion object {
//		const val APPLICATION_ID = "3YM4HY2EQZ"
//		const val API_KEY = "2b198fbbea989902b88cf07a8236202c"
//		const val ALGOLIA_INDEX_NAME_GROUPS = "groups"
//	}
}