package com.geektechkb.feature_main.presentation.ui.fragments

import androidx.core.view.GravityCompat
import androidx.core.view.isGone
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentMainFlowBinding
import com.geektechkb.feature_main.presentation.ui.fragments.settings.MainFlowViewModel
import com.geektechkb.feature_main.databinding.NavHeaderMainBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFlowFragment : BaseFlowFragment(
    R.layout.fragment_main_flow,
    R.id.nav_host_fragment_content_main,
) {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding by viewBinding(FragmentMainFlowBinding::bind)
    private val viewModel: MainFlowViewModel by viewModels()
    override fun setupNavigation(navController: NavController) {

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.nav_groups, R.id.nav_calls, R.id.nav_settings,
            ), binding.drawerLayout
        )
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.chatFragment -> binding.appBarMain.toolbarButto.isGone = true
                else -> binding.appBarMain.toolbarButto.isGone = false
            }
        }

        binding.appBarMain.toolbarButto.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.appBarMain.toolbarButton.setOnClickListener {

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