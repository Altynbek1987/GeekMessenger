package com.geektechkb.feature_main.presentation.ui.fragments.home

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
import androidx.navigation.fragment.findNavController
import androidx.paging.filter
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.algolia.instantsearch.android.list.autoScrollToStart
import com.algolia.instantsearch.android.paging3.flow
import com.algolia.instantsearch.android.searchbox.SearchBoxViewAppCompat
import com.algolia.instantsearch.core.connection.ConnectionHandler
import com.algolia.instantsearch.searchbox.connectView
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.extensions.navigateSafely
import com.geektechkb.core.utils.CheckInternet
import com.geektechkb.core.utils.CustomTypefaceSpan
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentHomeBinding
import com.geektechkb.feature_main.presentation.ui.adapters.UsersAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    R.layout.fragment_home
) {
    override val binding by viewBinding(FragmentHomeBinding::bind)
    override val viewModel by viewModels<HomeViewModel>()
    private val usersAdapter = UsersAdapter(this::onItemClick)
    private lateinit var cld: CheckInternet
    private val connectionHandler = ConnectionHandler()

    override fun initialize() {
        instantiateAdapter()
        checkInternet()
        initializeSearchBoxAndSetItsConnection()
    }

    private fun instantiateAdapter() = with(binding.recyclerview) {
        adapter = usersAdapter
        layoutManager = LinearLayoutManager(context)
        autoScrollToStart(usersAdapter)
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

    private fun initializeSearchBoxAndSetItsConnection() {
        val searchBoxView = SearchBoxViewAppCompat(binding.searchViewUsers)
        connectionHandler += viewModel.searchBox.connectView(searchBoxView)
    }

    override fun assembleViews() {
        renderToolbarTitle()
        modifySearchView()
        changeViewsVisibilityOnSearchViewManipulations()
    }

    private fun renderToolbarTitle() {
        binding.tvToolbarTitle.text = transformTextFont()
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
        binding.searchViewUsers.queryHint =
            transformTextFont(
                getString(R.string.search),
                R.font.avenir_next_regular,
                0,
                8
            )
    }

    private fun changeSearchEditTextTextColorAndHintTextColor() {
        val searchEditText: EditText =
            binding.searchViewUsers.findViewById(androidx.appcompat.R.id.search_src_text)
        searchEditText.setTextColor(Color.parseColor("#2C2C2C"))
        searchEditText.setTextColor(Color.parseColor("#B2B5B8"))
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
        clearSearchViewQueryAndFocus()
        binding.btnGroup?.setOnClickListener {
            findNavController().navigateSafely(R.id.action_homeFragment_to_groupListFragment)
        }
    }

    private fun clearSearchViewQueryAndFocus() {
        binding.imClear.setOnClickListener {
            binding.searchViewUsers.setQuery(null, true)
            binding.searchViewUsers.clearFocus()
        }
    }

    override fun launchObservers() {
        subscribeToUsers()
    }

    private fun subscribeToUsers() {
        viewModel.paginator.flow.spectatePaging {
            val usersExcludingTheCurrent =
                it.filter { user -> user.phoneNumber != viewModel.getCurrentUserPhoneNumber() }
            usersAdapter.submitData(usersExcludingTheCurrent)
        }
    }

    private fun onItemClick(phoneNumber: String?) {
        findNavController().directionsSafeNavigation(
            HomeFragmentDirections.actionHomeFragmentToChatFragment(
                phoneNumber
            )
        )
    }

    private fun transformTextFont(
        textToTransform: String = getString(R.string.app_name),
        @FontRes fontId: Int = R.font.avenir_next_bold,
        startIndex: Int = 4,
        endIndex: Int = 13
    ): SpannableStringBuilder {
        val span = SpannableStringBuilder(textToTransform)
        val typeface =
            ResourcesCompat.getFont(requireContext(), fontId)
        span.setSpan(
            CustomTypefaceSpan(typeface),
            startIndex, endIndex,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        return span
    }

    override fun onDestroyView() {
        super.onDestroyView()
        connectionHandler.clear()
    }
}