package com.geektechkb.feature_main.presentation.ui.fragments.group

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.navigateSafely
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding, GroupViewModel>(
    R.layout.fragment_group

) {

    override val binding by viewBinding(FragmentGroupBinding::bind)
    override val viewModel by viewModels<GroupViewModel>()
    override fun assembleViews() {

        binding.toolbarButton.setOnClickListener {
            findNavController().navigateUp()
        }
    override val galleryViewModel by viewModels<GroupViewModel>()
    override fun setupListeners() {
        binding.toolbarBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

}