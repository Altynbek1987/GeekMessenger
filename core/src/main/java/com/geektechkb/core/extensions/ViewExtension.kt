package com.geektechkb.core.extensions

import android.view.View
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.geektechkb.core.ui.state.UIState
import com.geektechkb.core.utils.OnSingleClickListener

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setOnSingleClickListener(l: (View) -> Unit) {
    setOnClickListener(OnSingleClickListener(l))
}

fun <T> ProgressBar.bindToUIStateLoading(uiState: UIState<T>) {
    isVisible = uiState is UIState.Loading
}