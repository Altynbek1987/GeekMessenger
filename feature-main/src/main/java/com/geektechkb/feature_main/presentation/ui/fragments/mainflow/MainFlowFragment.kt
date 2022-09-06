package com.geektechkb.feature_main.presentation.ui.fragments.mainflow

import android.annotation.SuppressLint
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentMainFlowBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainFlowFragment : BaseFlowFragment(
    R.layout.fragment_main_flow,
    R.id.nav_host_fragment_content_main,
) {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding by viewBinding(FragmentMainFlowBinding::bind)
    private val viewModel: MainFlowViewModel by viewModels()
    private var username: String? = null
    private var savedUserStatus: String? = null

    @Inject
    lateinit var preferences: UserPreferencesHelper

    @SuppressLint("ResourceType")
    override fun setupNavigation(navController: NavController) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.nav_groups, R.id.nav_calls, R.id.profileFragment,

                ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.chatFragment -> binding.homeAppBarMain.toolbarButton.isGone = true
                R.id.profileFragment -> binding.homeAppBarMain.toolbarButton.isGone = true
                R.id.cropPhotoFragment -> binding.homeAppBarMain.toolbarButton.isGone = true
                R.id.nav_groups -> binding.homeAppBarMain.toolbarButton.isGone = true
                R.id.nav_calls -> binding.homeAppBarMain.toolbarButton.isGone = true
                R.id.nav_host_fragment_content_main -> binding.homeAppBarMain.toolbarButton.isGone =
                    true
                R.id.action_chatFragment_to_deniedPermissionsDialogFragment -> binding.homeAppBarMain.toolbarButton.isGone =
                    true
                R.id.galleryBottomSheetFragment -> binding.homeAppBarMain.toolbarButton.isGone =
                    true
                R.id.homeFragment -> binding.homeAppBarMain.toolbarButton.isGone = false

                R.id.chatFragment, R.id.voiceCallFragment, R.id.incomingCallFragment, R.id.nav_groups, R.id.nav_calls, R.id.profileFragment -> binding.homeAppBarMain.toolbarButton.isGone =
                    true
                else -> binding.homeAppBarMain.toolbarButton.isGone = false
            }
        }

        binding.homeAppBarMain.toolbarButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }


    override fun establishRequest() {
        fetchUser()

    }

    private fun fetchUser() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.fetchUser(preferences.currentUserPhoneNumber)
        }
    }

    override fun launchObservers() {
        subscribeToUser()
    }


    private fun subscribeToUser() {
        viewModel.userState.spectateUiState(success = {
            savedUserStatus = it.lastSeen
            binding.nav.userNumber.text = (it.phoneNumber)
            binding.nav.imageProfile.loadImageWithGlide(it.profileImage)
            binding.nav.userName.text = it.name
            username = it.name
        })
        Log.e("anime", viewModel.userState.toString())
    }


    override fun onStart() {
        super.onStart()
        viewModel.updateUserStatus("В сети")
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateUserStatus("был(а) в ${formatCurrentUserTime("HH:mm")}")
    }
}