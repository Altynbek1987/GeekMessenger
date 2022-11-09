package com.geektechkb.feature_main.presentation

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants.DAY_MONTH_YEAR_HOURS_AND_MINUTES_DATE_FORMAT
import com.geektechkb.common.constants.Constants.HOURS_MINUTES_DATE_FORMAT
import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.core.extensions.loadImageAndSetInitialsIfFailed
import com.geektechkb.core.extensions.overrideOnBackPressed
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.data.local.preferences.PreferencesHelper
import com.geektechkb.feature_main.databinding.FragmentMainFlowBinding
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
    private var userStatus: String? = null

    @Inject
    lateinit var preferences: UserPreferencesHelper

    @Inject
    lateinit var preferencesHelper: PreferencesHelper

    override fun assembleViews() {
        changeTheme()
    }

    private fun changeTheme() {
        if (!preferencesHelper.isLightMode) {
            binding.nav.sunBtn.setImageResource(R.drawable.ic_baseline_nights_stay_24)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            binding.nav.sunBtn.setImageResource(R.drawable.ic_sun)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    override fun setupNavigation(navController: NavController) {
        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.nav_groups, R.id.profileFlowFragment,

                ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chatFragment, R.id.groupChatFragment, R.id.nav_groups, R.id.profileFlowFragment, R.id.profileFragment, R.id.photoPreviewFragment, R.id.videoPreviewFragment, R.id.createGroupFragment -> binding.homeAppBarMain.toolbarButton.isGone =
                    true
                else -> binding.homeAppBarMain.toolbarButton.isGone = false
            }
        }

        binding.homeAppBarMain.toolbarButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun setupListeners() {
        binding.nav.sunBtn.setOnClickListener {
            if (!preferencesHelper.isLightMode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.nav.sunBtn.setImageResource(R.drawable.ic_sun)
                preferencesHelper.isLightMode = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.nav.sunBtn.setImageResource(R.drawable.ic_baseline_nights_stay_24)
                preferencesHelper.isLightMode = false
            }
        }
        overrideOnBackPressed {
            requireActivity().finish()
        }
    }

    override fun establishRequest() {
        fetchUser()
    }

    private fun fetchUser() {
        viewModel.fetchUser(preferences.currentUserPhoneNumber)
    }

    override fun launchObservers() {
        subscribeToUser()
    }

    private fun subscribeToUser() = with(binding.nav) {
        viewModel.userState.spectateUiState(success = { user ->
            user.apply {
                userStatus = user.lastSeen
                userNumber.text = getString(
                    R.string.plus, phoneNumber?.substringAfter(
                        "+"
                    )?.chunked(3)?.joinToString(" ")
                )
                avatarView.loadImageAndSetInitialsIfFailed(
                    profileImage,
                    name,
                    binding.nav.cpiCurrentUserAvatar
                )
                userName.text = name
            }
        })
    }

    override fun onStart() {
        super.onStart()
        viewModel.updateUserStatus("в сети")
    }

    override fun onStop() {
        super.onStop()
        viewModel.updateUserStatus(
            "был(а) в ${
                formatCurrentUserTime(
                    DAY_MONTH_YEAR_HOURS_AND_MINUTES_DATE_FORMAT
                ).format(HOURS_MINUTES_DATE_FORMAT)
            }"
        )
    }
}