package com.geektechkb.feature_main.presentation.ui.fragments.settings

import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding, SettingsViewModel>(
    R.layout.fragment_settings
) {
    override val binding by viewBinding(FragmentSettingsBinding::bind)
    override val viewModel by viewModels<SettingsViewModel>()
}