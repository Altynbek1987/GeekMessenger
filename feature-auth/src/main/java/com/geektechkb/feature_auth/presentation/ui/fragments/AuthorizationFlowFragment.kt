package com.geektechkb.feature_auth.presentation.ui.fragments

import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.feature_auth.R
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class AuthorizationFlowFragment : BaseFlowFragment(
    R.layout.fragment_authorization_flow,
    R.id.nav_host_fragment_container_auth
)