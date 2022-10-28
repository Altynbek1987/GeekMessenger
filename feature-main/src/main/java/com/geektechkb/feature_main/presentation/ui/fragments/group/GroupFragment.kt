package com.geektechkb.feature_main.presentation.ui.fragments.group

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.android.paging3.flow
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentGroupBinding
import com.geektechkb.feature_main.domain.models.User
import com.geektechkb.feature_main.presentation.ui.adapters.UsersGroupAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.profil.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding, GroupViewModel>(
    R.layout.fragment_group
) {
	override val binding by viewBinding(FragmentGroupBinding::bind)
	override val viewModel by viewModels<GroupViewModel>()
	private val profileViewModel by viewModels<ProfileViewModel>()
	private val groupAdapter = UsersGroupAdapter(this::onItemClick)
	private var userCount = 1
	private var userNumber: String? = null
	private val userList = mutableListOf<User>()

	@Inject
	lateinit var preferences: UserPreferencesHelper


	override fun initialize() {
		instantiateAdapter()
	}

	private fun instantiateAdapter() = with(binding.recyclerviewGroup) {
		adapter = groupAdapter
		layoutManager = LinearLayoutManager(context)
		autoScrollToStart(groupAdapter)
		itemAnimator = null
	}

	override fun establishRequest() {
		fetchUser()
	}

	private fun fetchUser() {
		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			profileViewModel.fetchUser(preferences.currentUserPhoneNumber)
		}


	}

	override fun setupListeners() {
		binding.toolbarBtn.setOnClickListener {
			findNavController().navigateUp()
		}
		binding.openUsersGroup.setOnClickListener {
			Log.e("assembleViews", userList.toString())
			findNavController().navigate(
				GroupFragmentDirections.actionNavGroupsToCreateGroupFragment(
					preferences.currentUserPhoneNumber,
					userList.toTypedArray(), userCount
				)
			)
		}
	}

	override fun launchObservers() {
		subscribeToUsers()
	}


	private fun subscribeToUsers() {
		viewModel.paginator.flow.spectatePaging {
			val usersExcludingTheCurrent =
				it.filter { user -> user.phoneNumber != viewModel.getCurrentUserPhoneNumber() }
			groupAdapter.submitData(usersExcludingTheCurrent)
		}

	}


	private fun onItemClick(user: User, checked: Boolean) {
		if (checked) {
			userCount++
			userList.add(0, user)
			Log.e("TAG", "onItemClick: $userList")
		} else {
			userCount--
			userList.remove(user)
		}
		binding.openUsersGroup.isVisible = userCount != 0


	}

	override fun onDestroyView() {
		super.onDestroyView()
		userList.clear()
	}
}


