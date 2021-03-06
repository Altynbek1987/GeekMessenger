package com.geektechkb.feature_main.presentation.ui.fragments.calls

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentCallsBinding

class CallsFragment : BaseFragment<FragmentCallsBinding, СallsViewModel>(
    R.layout.fragment_calls
) {
    override val binding by viewBinding(FragmentCallsBinding::bind)
    override val viewModel by viewModels<СallsViewModel>()
}