package com.geektechkb.feature_main.presentation.ui.fragments.crop

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.util.Base64
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.extensions.directionsSafeNavigation
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentCropPhotoBinding
import com.geektechkb.feature_main.presentation.ui.fragments.crop.transformations.MirrorTransformation
import com.geektechkb.feature_main.presentation.ui.models.enums.CropPhotoRequest
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class CropPhotoFragment :
    BaseFragment<FragmentCropPhotoBinding, CropPhotoViewModel>(R.layout.fragment_crop_photo) {
    override val binding by viewBinding(FragmentCropPhotoBinding::bind)
    override val viewModel by viewModels<CropPhotoViewModel>()
    private var uri: Uri = Uri.EMPTY
    private var target: CropPhotoTarget? = null
    private val args by navArgs<CropPhotoFragmentArgs>()
    private var rotateDegrees = 0F
    private var shouldFlipImage = true

    override fun initialize() {
        getData()
    }

    override fun assembleViews() {
        applyCropViewOverlayColor()
    }

    private fun applyCropViewOverlayColor() {
        binding.kropView.applyOverlayColor(Color.parseColor("#B22C2C2C"))
    }

    private fun getData() {
        uri = Uri.parse(args.uri)
        showCrop()
        true.loadUri()
    }

    override fun setupListeners() {
        rotateImage()
        flipImage()
        navigateBackToAccordingFragment()
    }

    private fun navigateBackToAccordingFragment() {
        binding.btnResult.setOnClickListener {
            when (args.whereToNavigateBack) {
                CropPhotoRequest.PROFILE -> findNavController().directionsSafeNavigation(
                    CropPhotoFragmentDirections.actionCropPhotoFragmentToProfileFragment(
                        getCroppedImage()

                    )
                )
                CropPhotoRequest.EDIT_PROFILE -> findNavController().directionsSafeNavigation(
                    CropPhotoFragmentDirections.actionCropPhotoFragmentToEditProfileFragment(
                        getCroppedImage()
                    )
                )
            }
        }
    }

    private fun rotateImage() {
        binding.imRotateImage.setOnClickListener {
            rotateDegrees -= 90F
            target = CropPhotoTarget(binding.cropContainer, binding.kropView, true)
            Picasso
                .get()
                .load(uri)
                .centerInside()
                .resize(3000, 3000)
                .noFade()
                .priority(Picasso.Priority.LOW)
                .rotate(rotateDegrees)
                .into(target!!)
        }
    }

    private fun flipImage() {
        binding.imFlipImage.setOnClickListener {
            shouldFlipImage = when (shouldFlipImage) {
                true -> {
                    target = CropPhotoTarget(binding.cropContainer, binding.kropView, true)
                    Picasso
                        .get()
                        .load(uri)
                        .centerInside()
                        .resize(3000, 3000)
                        .noFade()
                        .priority(Picasso.Priority.LOW)
                        .rotate(rotateDegrees)
                        .transform(MirrorTransformation())
                        .into(target!!)
                    false
                }
                false -> {
                    target = CropPhotoTarget(binding.cropContainer, binding.kropView, true)
                    Picasso
                        .get()
                        .load(uri)
                        .centerInside()
                        .resize(3000, 3000)
                        .noFade()
                        .priority(Picasso.Priority.LOW)
                        .rotate(rotateDegrees)
                        .into(target!!)
                    true
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            uri = data?.data ?: return
            true.loadUri()
        }
    }

    private fun Boolean.loadUri() {
        target = CropPhotoTarget(binding.cropContainer, binding.kropView, this)
        Picasso
            .get()
            .load(uri)
            .centerInside()
            .resize(3000, 3000)
            .noFade()
            .priority(Picasso.Priority.LOW)
            .rotate(0F)
            .into(target!!)
    }

    private fun showCrop() {
        binding.viewFlipper.displayedChild = 0
        binding.kropView.applyOverlayColor(R.color.white)
        activity?.invalidateOptionsMenu()
        binding.kropView.applyOverlayShape(0)
    }

    private fun getCroppedImage(): String {
        val bitmap = binding.kropView.getCroppedBitmap()
        activity?.invalidateOptionsMenu()
        binding.kropView.applyOverlayColor(Color.TRANSPARENT)
        binding.viewFlipper.displayedChild = 1
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 90, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}