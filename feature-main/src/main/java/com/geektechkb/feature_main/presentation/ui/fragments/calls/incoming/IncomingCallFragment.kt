package com.geektechkb.feature_main.presentation.ui.fragments.calls.incoming

import android.os.Build
import android.view.DragEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentIncomingCallBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IncomingCallFragment :
    BaseFragment<FragmentIncomingCallBinding, IncomingCallViewModel>(R.layout.fragment_incoming_call) {
    override val binding by viewBinding(FragmentIncomingCallBinding::bind)
    override val viewModel by viewModels<IncomingCallViewModel>()

    @RequiresApi(Build.VERSION_CODES.N)
    override fun setupListeners() {
        binding.imAcceptCall.setOnClickListener {
            viewModel.acceptAnIncomingCall()
        }
        binding.imHangUpCall.setOnDragListener { v, event ->
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    v.startDragAndDrop(
                        null,
                        View.DragShadowBuilder(binding.imHangUpCall),
                        null,
                        0
                    )
                    binding.imHangUpCall.isVisible = false
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    v.invalidate()
                    binding.imHangUpCall.isVisible = true
                    true
                }
                else -> {
                    false
                }
            }

        }
    }

}