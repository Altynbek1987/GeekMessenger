package com.geektechkb.feature_main.presentation.ui.fragments.group

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentGroupBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupFragment : BaseFragment<FragmentGroupBinding, GroupViewModel>(
    R.layout.fragment_group

) {

    override val binding by viewBinding(FragmentGroupBinding::bind)
    override val galleryViewModel by viewModels<GroupViewModel>()
    override fun assembleViews() {
    }

}