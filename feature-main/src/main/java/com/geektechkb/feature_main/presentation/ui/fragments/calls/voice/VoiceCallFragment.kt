package com.geektechkb.feature_main.presentation.ui.fragments.calls.voice

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.changeIconWhenActivated
import com.geektechkb.core.extensions.navigateSafely
import com.geektechkb.core.extensions.setOnSingleClickListener
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentVoiceCallBinding

class VoiceCallFragment :
    BaseFragment<FragmentVoiceCallBinding, VoiceCallViewModel>(R.layout.fragment_voice_call) {
    override val binding by viewBinding(FragmentVoiceCallBinding::bind)
    override val galleryViewModel by viewModels <VoiceCallViewModel>()
    private val args by navArgs<VoiceCallFragmentArgs>()
    override fun establishRequest() {
        changeButtonsImageWhenActivated()
        setCalleeUsername()
    }


    private fun changeButtonsImageWhenActivated() {
        binding.imSpeaker.changeIconWhenActivated(binding.imStartVideo, binding.imMute)
    }

    private fun setCalleeUsername() {
        binding.tvUsername.text = args.username
    }


    override fun setupListeners() {
        binding.imBack.setOnSingleClickListener {
            findNavController().navigateSafely(R.id.action_voiceCallFragment_to_incomingCallFragment)
        }
    }


}