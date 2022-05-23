package com.geektechkb.feature_auth.presentation.ui.extensions

import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.geektechkb.feature_auth.R

fun Fragment.mainNavController() =
    requireActivity().findNavController(R.id.nav_host_fragment_container_auth)