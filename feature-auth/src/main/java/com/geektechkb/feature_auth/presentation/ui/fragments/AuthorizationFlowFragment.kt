package com.geektechkb.feature_auth.presentation.ui.fragments

import androidx.navigation.NavController
import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.feature_auth.R
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthorizationFlowFragment : BaseFlowFragment(
    R.layout.fragment_authorization_flow,
    R.id.nav_host_fragment_container_auth
) {
    @Inject
    lateinit var onBoardPreferencesHelper: OnBoardPreferencesHelper

    override fun setupNavigation(navController: NavController) {

        when (onBoardPreferencesHelper.hasOnBoardBeenShown) {
            false -> {
                navController.navigate(R.id.onBoardFragment)
            }
            else -> {
                navController.navigateUp()
            }
        }
    }
}