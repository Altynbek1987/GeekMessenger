package com.geektechkb.feature_main.presentation.ui.fragments

import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFlowFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentMainFlowBinding
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFlowFragment : BaseFlowFragment(
    R.layout.fragment_main_flow,
    R.id.nav_host_fragment_content_main,
) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding by viewBinding(FragmentMainFlowBinding::bind)
    override fun setupNavigation(navController: NavController) {

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment,
            R.id.nav_groups, R.id.nav_calls, R.id.nav_settings), drawerLayout)
        navView.setupWithNavController(navController)

        binding.appBarMain.toolbarButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }
}