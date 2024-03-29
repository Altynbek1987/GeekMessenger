package com.geektechkb.geekmessenger.presentation.ui.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.geektechkb.feature_auth.data.local.preferences.AuthorizePreferences
import com.geektechkb.feature_auth.data.local.preferences.OnBoardPreferencesHelper
import com.geektechkb.feature_main.data.local.preferences.LocaleHelper
import com.geektechkb.geekmessenger.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var navController: NavController

    @Inject
    lateinit var userPreferences: AuthorizePreferences



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
            true -> {
                navGraph.setStartDestination(R.id.mainFlowFragment)
            }
            false -> {
                navGraph.setStartDestination(R.id.authorizationFlowFragment)
            }

        }
        navController.graph = navGraph
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(com.geektechkb.feature_main.R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(LocaleHelper(newBase).loadLocale(newBase))
    }
}