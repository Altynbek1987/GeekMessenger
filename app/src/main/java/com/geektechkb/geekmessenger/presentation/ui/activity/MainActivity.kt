package com.geektechkb.geekmessenger.presentation.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.utils.AppTheme
import com.geektechkb.core.utils.Coordinate
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

        when (userPreferences.isAuthorize) {
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
    private var liveTheme: MutableLiveData<AppTheme> = MutableLiveData()
    private lateinit var activity: BaseActivity

    companion object {
        val instance = MainActivity()
    }

    fun init(activity: BaseActivity, defaultTheme: AppTheme) {
        if (liveTheme.value == null) {
            this.liveTheme.value = defaultTheme
        }
        this.activity = activity
    }

    fun setActivity(activity: BaseActivity) {
        this.activity = activity
    }

    fun getCurrentTheme(): AppTheme? {
        return getCurrentLiveTheme().value
    }

    fun getCurrentLiveTheme(): MutableLiveData<AppTheme> {
        return liveTheme
    }

    fun reverseChangeTheme(newTheme: AppTheme, view: View, duration: Long = 600) {
        changeTheme(newTheme, getViewCoordinates(view), duration, true)
    }

    fun changeTheme(newTheme: AppTheme, view: View, duration: Long = 600) {
        changeTheme(newTheme, getViewCoordinates(view), duration)
    }

    fun changeTheme(
        newTheme: AppTheme,
        sourceCoordinate: Coordinate,
        duration: Long = 600,
        isRevers: Boolean = false
    ) {

        if (getCurrentTheme()?.id() == newTheme.id() || activity.isRunningChangeThemeAnimation()) {
            return
        }

        //start animation
        activity.changeTheme(newTheme, sourceCoordinate, duration, isRevers)

        //set LiveData
        getCurrentLiveTheme().value = newTheme
    }
    fun getViewCoordinates(view: View): Coordinate {
        return Coordinate(
            getRelativeLeft(view) + view.width / 2,
            getRelativeTop(view) + view.height / 2
        )
    }

    private fun getRelativeLeft(myView: View): Int {
        return if ((myView.parent as View).id == BaseActivity.ROOT_ID) myView.left else myView.left + getRelativeLeft(
            myView.parent as View
        )
    }

    private fun getRelativeTop(myView: View): Int {
        return if ((myView.parent as View).id == BaseActivity.ROOT_ID) myView.top else myView.top + getRelativeTop(
            myView.parent as View
        )
    }
}