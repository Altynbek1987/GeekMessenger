package com.geektechkb.feature_auth.presentation.ui.fragments.onboard

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import com.geektechkb.feature_auth.databinding.FragmentOnBoardBinding
import com.geektechkb.feature_auth.presentation.ui.adapter.ViewPagerAdapter
import com.geektechkb.feature_auth.presentation.ui.model.BoardModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OnBoardFragment :
    BaseFragment<FragmentOnBoardBinding, OnBoarViewModel>(R.layout.fragment_on_board) {
    override val binding by viewBinding(FragmentOnBoardBinding::bind)
    override val viewModel: OnBoarViewModel by viewModels()

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
        viewPagerAdapter.setData(list = getBoardList())
        binding.dotsIndicator.attachTo(binding.pager)

    }

    private fun getBoardList(): ArrayList<BoardModel> {
        val list: ArrayList<BoardModel> = arrayListOf()
        list.add(
            BoardModel(
                R.drawable.ic_android_teacher1,
                "GeekMessage",
                "Добро пожаловать в GeekMessage",
                "NEXT"
            )
        )
        list.add(
            BoardModel(
                R.drawable.ic_group_1, "Описание", "Отличный и удобный messanger",
                "NEXT"
            )
        )
        list.add(
            BoardModel(
                R.drawable.ic_illustrtionsof404,
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
            }
            1 -> {
                binding.pager.setCurrentItem(2, true)
            }
            2 -> {
                preferences.hasOnBoardBeenShown = true
                findNavController().navigate(R.id.action_onBoardFragment_authWithPhoneNumber)

            }
        }


    }


}
