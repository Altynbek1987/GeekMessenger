package com.geektechkb.feature_auth.presentation.ui.fragments.auth.onboard

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import com.geektechkb.feature_auth.databinding.FragmentOnBoardBinding
import com.geektechkb.feature_auth.presentation.model.BoardModel
import com.geektechkb.feature_auth.presentation.ui.adapter.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardFragment :
    BaseFragment<FragmentOnBoardBinding, OnBoardViewModel>(R.layout.fragment_on_board) {
    override val binding by viewBinding(FragmentOnBoardBinding::bind)
    override val viewModel: OnBoardViewModel by viewModels()
    private var currentPagerItemState: Int? = null

    private val viewPagerAdapter = ViewPagerAdapter(this::onItemClick)

    @Inject
    lateinit var preferences: OnBoardPreferencesHelper


    override fun setupListeners() {
        binding.pager.setOnClickListener {
            binding.pager.setCurrentItem(binding.pager.currentItem + 1, true)
        }
    }

    override fun assembleViews() {
        setupAdapter()
    }

    private fun setupAdapter() {
        binding.pager.adapter = viewPagerAdapter
        viewPagerAdapter.setData(getBoardList())
        binding.dotsIndicator.attachTo(binding.pager)

    }

    private fun getBoardList(): ArrayList<BoardModel> {
        val list: ArrayList<BoardModel> = arrayListOf()
        list.add(
            BoardModel(
                R.drawable.ic_group_24,
                "GeekMessage",
                "Добро пожаловать в GeekMessage",
                "NEXT"
            )
        )
        list.add(
            BoardModel(
                R.drawable.communicationpng, "Описание", "Отличный и удобный messanger",
                "NEXT"
            )
        )
        list.add(
            BoardModel(
                R.drawable.ideapng,
                " Погнали",
                "Скорее нажимай на кнопку",
                "START"
            )
        )

        return list
    }

    private fun onItemClick(position: Int) {
        when (position) {
            0 -> {
                binding.pager.setCurrentItem(1, true)
                currentPagerItemState = 1
            }
            1 -> {
                binding.pager.setCurrentItem(2, true)
                currentPagerItemState = 2
            }
            2 -> {
                preferences.hasOnBoardBeenShown = true
                findNavController().navigate(R.id.action_onBoardFragment_to_signUpFragment)
                currentPagerItemState = 3

            }

        }


    }


}
