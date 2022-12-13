package com.geektechkb.feature_main.presentation.ui.fragments.mediaPreview.photo

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.*
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentPhotoPreviewBinding
import com.geektechkb.feature_main.domain.models.enums.ChatMessageRequest
import com.geektechkb.feature_main.domain.models.enums.PreviewPhotoRequest
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PhotoPreviewFragment :
    BaseFragment<FragmentPhotoPreviewBinding, PhotoPreviewViewModel>(R.layout.fragment_photo_preview) {

    override val binding by viewBinding(FragmentPhotoPreviewBinding::bind)
    override val viewModel by viewModels<PhotoPreviewViewModel>()
    private val args by navArgs<PhotoPreviewFragmentArgs>()


    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper

    override fun assembleViews() {
        setSelectedPhoto()
        checkNavFragment()
    }

    private fun previewPhotoInitialize() {
        binding.tvImageCount.text = getString(R.string.media_count, args.photoCount.toString())
        binding.tvNameUser.text = args.phoneNumber
        binding.tvImDateTimeInfo.text = getString(
            R.string.time_photo, parseToFormat(args.time, "dd.mm.yy"), parseToFormat(args.time)
        )
    }

    private fun checkNavFragment() {
        when (args.photoPreview) {
            PreviewPhotoRequest.PHOTO -> {
                binding.containerBottomInfoSendPreview.invisible()
                binding.btnResult.invisible()
                binding.containerBottomInfoPreview.visible()
                previewPhotoInitialize()
            }
            PreviewPhotoRequest.SEND_PHOTO -> {
                binding.btnResult.visible()
                binding.containerBottomInfoSendPreview.visible()
                binding.containerBottomInfoPreview.invisible()
            }
        }
    }

    private fun setSelectedPhoto() {
        binding.imSelectedImage.loadImageWithGlide(args.photo)
    }

    override fun setupListeners() {
        binding.btnResult.setOnClickListener {
            when (args.chatMessage) {
                ChatMessageRequest.CHAT -> findNavController().navigate(
                    PhotoPreviewFragmentDirections.actionPhotoReviewFragmentToChatFragment(
                        args.phoneNumber, args.photo, "image"
                    )
                )
                ChatMessageRequest.GROUP_CHAT ->
                    if (binding.tvCaption.text.toString().isEmpty()) {
                        findNavController().navigate(
                            PhotoPreviewFragmentDirections.actionPhotoPreviewFragmentToGroupChatFragment(
                                groupName = args.groupName.toString(),
                                usersPhoneNumbers = args.usersPhoneNumber,
                                userCount = 0,
                                message = null,
                                userNumber = args.phoneNumber,
                                image = args.photo,
                                mediaType = "image"
                            )
                        )
                    } else {
                        findNavController().navigate(
                            PhotoPreviewFragmentDirections.actionPhotoPreviewFragmentToGroupChatFragment(
                                groupName = args.groupName.toString(),
                                usersPhoneNumbers = args.usersPhoneNumber,
                                userCount = 0,
                                message = binding.tvCaption.text.toString(),
                                userNumber = args.phoneNumber,
                                image = args.photo,
                                mediaType = "image"
                            )
                        )
                    }
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigate(
                PhotoPreviewFragmentDirections.actionPhotoPreviewFragmentToGroupChatFragment(
                    groupName = args.groupName.toString(),
                    usersPhoneNumbers = args.usersPhoneNumber,
                    userCount = 0,
                    userNumber = args.phoneNumber,
                    image = null,
                    mediaType = null
                )
            )
        }
        onBackPressed()
    }

    private fun onBackPressed() {
        overrideOnBackPressed {
            findNavController().navigate(
                PhotoPreviewFragmentDirections.actionPhotoPreviewFragmentToGroupChatFragment(
                    groupName = args.groupName.toString(),
                    usersPhoneNumbers = args.usersPhoneNumber,
                    userCount = 0,
                    userNumber = args.phoneNumber,
                    image = null,
                    mediaType = null
                )
            )
        }
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}