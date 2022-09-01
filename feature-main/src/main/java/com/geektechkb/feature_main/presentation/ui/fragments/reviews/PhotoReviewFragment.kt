package com.geektechkb.feature_main.presentation.ui.fragments.reviews

import android.net.Uri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentPhotoPreviewBinding


class PhotoReviewFragment :
    BaseFragment<FragmentPhotoPreviewBinding, PhotoReviewViewModel>(R.layout.fragment_photo_preview) {

    override val binding by viewBinding(FragmentPhotoPreviewBinding::bind)
    override val galleryViewModel by viewModels<PhotoReviewViewModel> ()
    private var uri: Uri = Uri.EMPTY
    private val args by navArgs<PhotoReviewFragmentArgs>()

    override fun assembleViews() {
        uri = Uri.parse(args.photo)
        binding.imageContent.loadImageWithGlide(uri)
    }

}