package com.geektechkb.core.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.viewbinding.ViewBinding
import com.geektechkb.core.R

abstract class BaseDialogFragmentWithoutViewModel<Binding : ViewBinding>(
    @LayoutRes private val layoutId: Int
) : AppCompatDialogFragment() {

    protected abstract val binding: Binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.setCancelable(false)
        dialog?.setCanceledOnTouchOutside(false)

        setStyle(STYLE_NO_TITLE, R.style.CustomDialog)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setStyle(STYLE_NO_FRAME, androidx.appcompat.R.style.Base_Theme_AppCompat_Dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        return inflater.inflate(layoutId, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        assembleViews()
        initialize()
        setupListeners()
    }

    protected open fun initialize() {
    }

    protected open fun assembleViews() {

    }

    protected open fun setupListeners() {
    }


}