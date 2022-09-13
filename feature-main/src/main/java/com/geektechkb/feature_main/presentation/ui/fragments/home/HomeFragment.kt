package com.geektechkb.feature_main.presentation.ui.fragments.home

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.TypefaceSpan
import android.util.Log
import android.widget.EditText
import android.widget.SearchView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.core.utils.CheckInternet
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

    override fun initialize() {
        instantiateAdapter()
    }

    private fun instantiateAdapter() {
        binding.recyclerview.adapter = usersAdapter
        binding.recyclerview.layoutManager = LinearLayoutManager(context)
    }

    override fun setupListeners() {
        checkInternet()
        establishSearch()
    }


    override fun assembleViews() {
        renderToolbarTitle()
        modifySearchView()
        changeViewsVisibilityOnSearchViewManipulations()
    }

    private fun renderToolbarTitle() {
        binding.tvToolbarTitle.text =
            transformTitleSecondPartFontInToolbar()
    }

    private fun modifySearchView() {
        val queryHint: TextView =
            binding.searchViewUsers.findViewById(androidx.appcompat.R.id.search_src_text)
        val searchEditText: EditText =
            binding.searchViewUsers.findViewById(androidx.appcompat.R.id.search_src_text)
        queryHint.typeface =
            ResourcesCompat.getFont(requireContext(), com.geektechkb.core.R.font.roboto_medium_500)
        searchEditText.setTextColor(Color.parseColor("#2C2C2C"))
//        searchEditText.typeface = Typeface.createFromAsset(
//            resources.assets,
//            "avenir_next_regular.otf"
//        )
        searchEditText.setHintTextColor(Color.parseColor("#A9B5C3"))
        searchEditText
    }

    private fun changeViewsVisibilityOnSearchViewManipulations() = with(binding) {
        searchViewUsers.setOnQueryTextFocusChangeListener { _, hasFocus ->
            when (hasFocus) {
                true -> {
                    tvToolbarTitle.isGone = true
                    toolbarButton.isGone = true
                    imSearchIcon.isGone = false
                }
                else -> {
                    tvToolbarTitle.isGone = false
                    toolbarButton.isGone = false
                    imSearchIcon.isGone = true
                    searchViewUsers.onActionViewCollapsed()
                }
            }
        }
    }

    override fun launchObservers() {
        subscribeToUsers()
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

    private fun establishSearch() {
        binding.searchViewUsers.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
            }

            override fun onQueryTextChange(newText: String?): Boolean {
            }

        })
    }

    private fun subscribeToUsers() {
        viewModel.fetchPagedUsers().spectatePaging(success = {
            usersAdapter.submitData(it)
        })
    }

    private fun onItemClick(phoneNumber: String?) {
        findNavController().directionsSafeNavigation(
            HomeFragmentDirections.actionHomeFragmentToChatFragment(
                phoneNumber
            )
        )
    }

    private fun transformTitleSecondPartFontInToolbar(): SpannableStringBuilder {
        val span = SpannableStringBuilder("GeekMessenger")
        val typeface =
            Typeface.createFromAsset(requireActivity().assets, "avenir_next_bold.otf")
        val typefaceSpan = TypefaceSpan(typeface)
        span.setSpan(
            typefaceSpan,
            4, 13,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
        return span
    }
}