package com.geektechkb.core.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.utils.widget.ImageFilterButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.*
import androidx.viewbinding.ViewBinding
import com.geektechkb.core.ui.state.UIState
import com.google.android.material.progressindicator.CircularProgressIndicator
import io.reactivex.internal.util.NotificationLite.error
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

abstract class BaseFragment<Binding : ViewBinding, ViewModel : BaseViewModel>(@LayoutRes layoutId: Int) :
    Fragment(layoutId) {
    protected abstract val binding: Binding
    protected abstract val viewModel: ViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialize()
        assembleViews()
        setupListeners()
        establishRequest()
        launchObservers()
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


    protected fun <T : Any> Flow<PagingData<T>>.spectatePaging(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        success: suspend (data: PagingData<T>) -> Unit,
    ) {
        safeFlowGather(lifecycleState) {
            collectLatest {
                success(it)
                error(it)
            }
        }
    }

    protected fun <T : Any> StateFlow<UIState<PagingData<T>>>.spectatePagingData(
        lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
        success: ((data: PagingData<T>) -> Unit)? = null,
        loading: ((data: UIState.Loading<PagingData<T>>) -> Unit)? = null,
        error: ((error: String) -> Unit)? = null,
        idle: ((idle: UIState.Idle<PagingData<T>>) -> Unit)? = null,
        gatherIfSucceed: ((state: UIState<PagingData<T>>) -> Unit)? = null,

        ) {
        safeFlowGather(lifecycleState) {
            collectLatest {
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

    protected fun <T> UIState<T>.assembleViewVisibility(
        group: ConstraintLayout,
        loader: CircularProgressIndicator,
        navigationSucceed: Boolean = false,
    ) {
        fun displayLoader(isDisplayed: Boolean) {
            group.isVisible = isDisplayed
            loader.isVisible = isDisplayed
        }
        when (this) {
            is UIState.Idle -> {

            }
            is UIState.Loading -> {
                displayLoader(true)
            }
            is UIState.Error -> {
                displayLoader(false)
            }
            is UIState.Success -> {
                if (navigationSucceed) {
                    displayLoader(true)
                } else {
                    displayLoader(false)
                }

            }
        }

    }



    open protected fun File(imImageProfile: ImageFilterButton) {}
}