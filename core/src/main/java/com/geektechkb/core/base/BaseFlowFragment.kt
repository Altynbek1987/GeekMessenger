package com.geektechkb.core.base

import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.geektechkb.core.ui.state.UIState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseFlowFragment(
    @LayoutRes layoutId: Int,
    @IdRes private val navHostFragmentId: Int
) : Fragment(layoutId) {

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navHostFragment =
            childFragmentManager.findFragmentById(navHostFragmentId) as NavHostFragment
        val navController = navHostFragment.navController
        setupNavigation(navController)
        initialize()
        assembleViews()
        setupListeners()
        establishRequest()
        launchObservers()
    }


    protected open fun setupNavigation(navController: NavController) {
    }

    protected open fun initialize() {
    }

    protected open fun assembleViews() {
    }

    protected open fun setupListeners() {
    }


    protected open fun establishRequest() {

    }

    protected open fun launchObservers() {

    }


    protected fun <T> StateFlow<UIState<T>>.spectateUiState(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        success: ((data: T) -> Unit)? = null,
        loading: ((data: UIState.Loading<T>) -> Unit)? = null,
        error: ((error: String) -> Unit)? = null,
        idle: ((idle: UIState.Idle<T>) -> Unit)? = null,
        gatherIfSucceed: ((state: UIState<T>) -> Unit)? = null,
    ) {
        safeFlowGather(lifecycleState) {
            collect {
                gatherIfSucceed?.invoke(it)
                when (it) {
                    is UIState.Idle -> {
                        idle?.invoke(it)
                    }
                    is UIState.Loading -> {
                        loading?.invoke(it)
                    }
                    is UIState.Error -> {
                        error?.invoke(it.error)
                    }
                    is UIState.Success -> {
                        success?.invoke(it.data)
                    }
                }
            }

        }

    }

    private fun safeFlowGather(
        lifecycleState: Lifecycle.State,
        gather: suspend () -> Unit,
    ) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(lifecycleState) {
                gather()
            }
        }
    }


}