package com.geektechkb.feature_main.presentation

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
import com.geektechkb.feature_main.R
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

    override fun setupNavigation(navController: NavController) {

        val drawerLayout: DrawerLayout = binding.drawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.nav_groups, R.id.nav_calls, R.id.profileFragment,

                ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
				R.id.chatFragment, R.id.createGroupFragment, R.id.groupChatFragment,
				R.id.voiceCallFragment, R.id.incomingCallFragment, R.id.nav_groups, R.id.nav_calls, R.id.profileFragment, R.id.cropPhotoFragment, R.id.editProfileFragment -> binding.homeAppBarMain.toolbarButton.isGone =
					true
				else -> binding.homeAppBarMain.toolbarButton.isGone = false
			}
        }
        binding.homeAppBarMain.toolbarButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)

            binding.homeAppBarMain.toolbarButton.setOnClickListener {
                binding.drawerLayout.openDrawer(GravityCompat.START)
            }
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
                phoneNumber?.let { phoneNumber ->
                    userNumber.text = StringBuilder(phoneNumber.substring(0, 4)).append(" ")
                        .append(phoneNumber.substringAfter("+996"))
                }
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