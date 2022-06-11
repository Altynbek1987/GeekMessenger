package com.geektechkb.geekmessenger.presentation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import com.geektechkb.geekmessenger.R
import com.geektechkb.geekmessenger.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private lateinit var navController: NavController
    private val binding by viewBinding(ActivityMainBinding::bind)


    @Inject
    lateinit var userPreferences: AuthorizePreferences

    @Inject
    lateinit var onBoardPreferencesHelper: OnBoardPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_GeekMessenger)
        super.onCreate(savedInstanceState)
        setUpNavigation()
    }

    private fun setUpNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container_parent) as NavHostFragment
        navController = navHostFragment.navController

        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        when (userPreferences.isAuthorized) {
            false -> {
                navGraph.setStartDestination(R.id.authorizationFlowFragment)
            }
            true -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
        }
        navController.graph = navGraph
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(com.geektechkb.feature_main.R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun recreate() {
        super.recreate()
        setTheme(R.style.Theme_GeekMessenger)


    }

    override fun onRestart() {
        setTheme(R.style.Theme_GeekMessenger)

        super.onRestart()

    }


}