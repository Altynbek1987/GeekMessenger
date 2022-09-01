package com.geektechkb.feature_main.presentation.ui.fragments.reviews

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentPhotoPreviewBinding
import com.geektechkb.feature_main.presentation.ui.fragments.crop.CropPhotoFragmentDirections
import com.geektechkb.feature_main.presentation.ui.fragments.crop.CropPhotoTarget
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream


class PhotoReviewFragment :
    BaseFragment<FragmentPhotoPreviewBinding, PhotoReviewViewModel>(R.layout.fragment_photo_preview) {

    override val binding by viewBinding(FragmentPhotoPreviewBinding::bind)
    override val viewModel by viewModels<PhotoReviewViewModel>()
    private var uri: Uri = Uri.EMPTY
    private val args by navArgs<PhotoReviewFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        uri = Uri.parse(args.photo.toString())
        true.loadUri()
    }

    private fun Boolean.loadUri() {
        Picasso
            .get()
            .load(uri)
            .centerInside()
            .resize(3000, 3000)
            .noFade()
            .priority(Picasso.Priority.LOW)
            .rotate(0F, 0F, 0F)
    }


    override fun assembleViews() {
        uri = Uri.parse(args.photo)
        binding.imageContent.loadImageWithGlide(uri)
    }

    protected open fun getCroppedImage(): String {
        activity?.invalidateOptionsMenu()
        val byteArrayOutputStream = ByteArrayOutputStream()
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

    override fun setupListeners() {
        binding.btnResult.setOnClickListener {
            findNavController().navigate(
                PhotoReviewFragmentDirections.actionPhotoReviewFragmentToChatFragment(
                    getCroppedImage()
                )
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            true.loadUri()
            Log.e("aime", uri.toString())
        }
    }
}