package com.geektechkb.core.utils

import android.view.View

class OnSingleClickListener(listener: View.OnClickListener) : View.OnClickListener {

    private var onClickListener: View.OnClickListener = listener

    override fun onClick(v: View?) {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis >= previousClickTimeMillis + DELAY_MILLIS) {
            previousClickTimeMillis = currentTimeMillis
            onClickListener.onClick(v)
        }
    }

    companion object {
        private const val DELAY_MILLIS = 200L
        private var previousClickTimeMillis = 0L
    }
}