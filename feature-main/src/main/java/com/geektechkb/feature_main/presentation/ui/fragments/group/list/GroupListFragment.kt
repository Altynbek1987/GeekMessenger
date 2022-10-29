package com.geektechkb.feature_main.presentation.ui.fragments.group.list

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.FontRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.navigateSafely
import com.geektechkb.core.extensions.overrideOnBackPressed
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.core.utils.CustomTypefaceSpan
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentGroupListBinding
import com.geektechkb.feature_main.domain.models.Group
import com.geektechkb.feature_main.presentation.ui.adapters.GroupListAdapter
import com.geektechkb.feature_main.presentation.ui.fragments.profil.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GroupListFragment : BaseFragment<FragmentGroupListBinding, GroupListViewModel>(
	R.layout.fragment_group_list
) {
	override val binding by viewBinding(FragmentGroupListBinding::bind)
	override val viewModel by viewModels<GroupListViewModel>()
	private val profileViewModel by viewModels<ProfileViewModel>()
	private val groupAdapter = GroupListAdapter(this::onSelect)
	private lateinit var cld: CheckInternet
	private val connectionHandler = ConnectionHandler()

	// Lazy это отложно тоест создасть и сахранить его и когда надо injeck тит
	// preferences.get()
	@Inject
	lateinit var preferences: dagger.Lazy<UserPreferencesHelper>


	override fun initialize() {
		instantiateAdapter()
		checkInternet()
	}

	private fun instantiateAdapter() = with(binding.recyclerviewGroupList) {
		adapter = groupAdapter
		autoScrollToStart(groupAdapter)
		itemAnimator = null
	}

	private fun checkInternet() {
		activity?.application?.let {
			cld = CheckInternet(it)
		}
		cld.observe(viewLifecycleOwner) {
			if (it) {
				Log.e("connection", it.toString())
			} else {
				findNavController().navigate(R.id.action_homeFragment_to_connectionCheckFragment)
			}
		}
	}

	override fun assembleViews() {
		renderToolbarTitle()
		modifySearchView()
		changeViewsVisibilityOnSearchViewManipulations()
	}

	private fun renderToolbarTitle() {
		transformTextFont()
	}

	private fun modifySearchView() {
		changeQueryHintTextColorAndFont()
		changeSearchEditTextTextColorAndHintTextColor()
	}

	private fun changeQueryHintTextColorAndFont() {
		val queryHint: TextView =
			binding.searchViewUsers.findViewById(androidx.appcompat.R.id.search_src_text)
		queryHint.typeface =
			ResourcesCompat.getFont(requireContext(), com.geektechkb.core.R.font.roboto_medium_500)
		binding.searchViewUsers.queryHint = transformTextFont(
			getString(R.string.search), R.font.avenir_next_regular, 0, 8
		)
	}

	private fun changeSearchEditTextTextColorAndHintTextColor() {
		val searchEditText: EditText =
			binding.searchViewUsers.findViewById(androidx.appcompat.R.id.search_src_text)
		searchEditText.setTextColor(Color.parseColor("#2C2C2C"))
		searchEditText.setHintTextColor(Color.parseColor("#A9B5C3"))
	}

	private fun changeViewsVisibilityOnSearchViewManipulations() = with(binding) {
		searchViewUsers.setOnQueryTextFocusChangeListener { _, hasFocus ->
			when (hasFocus) {
				true -> {
					tvToolbarTitle.isGone = true
					toolbarButton.isGone = true
					imClear.isGone = false
					imSearchIcon.isGone = false
				}
				else -> {
					tvToolbarTitle.isGone = false
					toolbarButton.isGone = false
					imClear.isGone = true
					imSearchIcon.isGone = true
					searchViewUsers.onActionViewCollapsed()
				}
			}
		}
	}

	override fun setupListeners() {
		createGroups()
		clearSearchViewQueryAndFocus()
		onBackPress()
	}

	private fun onBackPress() {
		overrideOnBackPressed {
			findNavController().navigateSafely(R.id.action_groupListFragment_to_homeFragment)
		}
	}

	private fun createGroups() {
		binding.tvGroups.setOnClickListener {
			findNavController().navigate(GroupListFragmentDirections.actionGroupListFragmentToNavGroups())
		}
		binding.btnChat.setOnClickListener {
			findNavController().navigate(GroupListFragmentDirections.actionGroupListFragmentToHomeFragment())
		}
	}

	private fun onSelect(group: Group) {
		findNavController().navigate(
			GroupListFragmentDirections.actionGroupListFragmentToGroupChatFragment(
				group.groupName.toString(), arrayOf(
					group.groupPhoto.toString()
				)
			)
		)
	}

	private fun clearSearchViewQueryAndFocus() {
		binding.imClear.setOnClickListener {
			binding.searchViewUsers.setQuery(null, true)
			binding.searchViewUsers.clearFocus()
		}
	}

	override fun launchObservers() {
		subscribeToGroups()
	}

	override fun establishRequest() {
		fetchUser()
	}

	private fun fetchUser() {
		viewLifecycleOwner.lifecycleScope.launchWhenStarted {
			profileViewModel.fetchUser(preferences.get().currentUserPhoneNumber)
		}
	}

	private fun subscribeToGroups() {
		viewModel.groupsState.spectateUiState(
			error = {
				Log.e("error", it)
			},
			success = {
				val groupList = mutableListOf<Group>()
				it.map { group ->
					group.groupMembers?.map { user ->
						if (preferences.get().currentUserPhoneNumber == user.phoneNumber
							|| preferences.get().currentUserPhoneNumber == group.userNumber
						) {
							groupList.add(group)
						}
					}
				}
				if (groupList.isNotEmpty()) {
					groupAdapter.submitList(groupList.distinct())
				}
			}
		)
	}

	private fun transformTextFont(
		textToTransform: String = getString(R.string.app_name),
		@FontRes fontId: Int = R.font.avenir_next_bold,
		startIndex: Int = 4,
		endIndex: Int = 13
	): SpannableStringBuilder {
		val span = SpannableStringBuilder(textToTransform)
		val typeface = ResourcesCompat.getFont(requireContext(), fontId)
		span.setSpan(
			CustomTypefaceSpan(typeface), startIndex, endIndex, Spannable.SPAN_INCLUSIVE_INCLUSIVE
		)
		return span
	}

	override fun onDestroyView() {
		super.onDestroyView()
		connectionHandler.clear()
	}
}

