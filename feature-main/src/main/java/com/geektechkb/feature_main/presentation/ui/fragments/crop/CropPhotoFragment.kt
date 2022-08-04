package com.geektechkb.feature_main.presentation.ui.fragments.crop

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
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentCropPhotoBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
open class CropPhotoFragment :
    BaseFragment<FragmentCropPhotoBinding, CropPhotoViewModel>(R.layout.fragment_crop_photo) {
    override val binding by viewBinding(FragmentCropPhotoBinding::bind)
    override val galleryViewModel by viewModels<CropPhotoViewModel>()
    private var uri: Uri = Uri.EMPTY
    private var target: CropPhotoTarget? = null
    private val args by navArgs<CropPhotoFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
//        setupListeners()
    }


    private fun getData() {
        uri = Uri.parse(args.uri.toString())
        showCrop()
        true.loadUri()
    }

    override fun setupListeners() {
        binding.btnResult.setOnClickListener {
            findNavController().navigate(
                CropPhotoFragmentDirections.actionCropPhotoFragmentToProfileFragment(
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


    private fun Boolean.loadUri() {
        target = CropPhotoTarget(binding.cropContainer, binding.kropView, this)
        Picasso
            .get()
            .load(uri)
            .centerInside()
            .resize(3000, 3000)
            .noFade()
            .priority(Picasso.Priority.LOW)
            .rotate(90F, 90F, 0F)
            .into(target!!)
    }

    private fun showCrop() {
        binding.viewFlipper.displayedChild = 0
        binding.kropView.applyOverlayColor(R.color.white)
        activity?.invalidateOptionsMenu()
        binding.kropView.applyOverlayShape(0)
    }

    protected open fun getCroppedImage(): String {
        val bitmap = binding.kropView.getCroppedBitmap()
        activity?.invalidateOptionsMenu()
        binding.kropView.applyOverlayColor(Color.TRANSPARENT)
        binding.viewFlipper.displayedChild = 1
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }

}
