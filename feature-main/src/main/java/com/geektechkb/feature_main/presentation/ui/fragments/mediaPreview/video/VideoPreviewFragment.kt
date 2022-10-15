package com.geektechkb.feature_main.presentation.ui.fragments.mediaPreview.video

import android.net.Uri
import android.widget.MediaController
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentVideoPreviewBinding
import com.geektechkb.feature_main.presentation.ui.models.enums.PreviewVideoRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class VideoPreviewFragment :
    BaseFragment<FragmentVideoPreviewBinding, VideoPreviewViewModel>(R.layout.fragment_video_preview) {
    override val binding by viewBinding(FragmentVideoPreviewBinding::bind)
    override val viewModel by viewModels<VideoPreviewViewModel>()
    private val args by navArgs<VideoPreviewFragmentArgs>()

    @Inject
    lateinit var userPreferencesHelper: UserPreferencesHelper

    override fun assembleViews() {
        setChatteeUsername()
        setVideoToVideoView()
        checkNavFragment()
    }

    private fun checkNavFragment() = with(binding) {
        when (args.videoPreview) {
            PreviewVideoRequest.VIDEO -> {
                btnSend.gone()
                tvChatteeUsername.gone()
                containerBottomInfoPreviewSend.gone()
                containerBottomInfoPreview.visible()
                tvVideoCount.visible()
                previewVideoInitialize()
            }
            PreviewVideoRequest.SENT_VIDEO -> {
                btnSend.visible()
                tvChatteeUsername.visible()
                containerBottomInfoPreviewSend.visible()
                containerBottomInfoPreview.invisible()
                tvVideoCount.invisible()
            }
        }
    }

    private fun previewVideoInitialize() {
        binding.tvVideoCount.text = getString(R.string.media_count, args.videoCount.toString())
        binding.tvNameUser.text = args.chatteeUsername
        binding.tvVvDateTimeInfo.text =
            getString(
                R.string.time_photo,
                parseToFormat(args.time, "dd.mm.yy"),
                parseToFormat(args.time)
            )
    }

    private fun setChatteeUsername() {
        binding.tvChatteeUsername.text = args.chatteeUsername
    }

    private fun setVideoToVideoView() = with(binding) {
        val mediaController = MediaController(requireContext())
        mediaController.setAnchorView(vvSelectedVideo)
        vvSelectedVideo.keepScreenOn = true
        vvSelectedVideo.setMediaController(mediaController)
        vvSelectedVideo.setVideoURI(Uri.parse(args.selectedVideo))
        vvSelectedVideo.seekTo(1)
    }

    override fun setupListeners() {
        navigateBack()
        sendVideo()
    }

    private fun navigateBack() {
        binding.imBack.setOnSingleClickListener {
            findNavController().navigateUp()
        }
    }

    private fun sendVideo() {
        binding.btnSend.setOnClickListener {
            findNavController().directionsSafeNavigation(
                VideoPreviewFragmentDirections.actionVideoPreviewFragmentToChatFragment(
                    args.chatteePhoneNumber,
                    args.selectedVideo, "video", args.selectedVideoDuration
                )
            )
        }
    }

}