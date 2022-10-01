package com.geektechkb.feature_main.presentation.ui.fragments.reviews

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.geektechkb.common.constants.Constants
import com.geektechkb.core.base.BaseFragment
import com.geektechkb.core.data.local.preferences.UserPreferencesHelper
import com.geektechkb.core.extensions.formatCurrentUserTime
import com.geektechkb.core.extensions.generateRandomId
import com.geektechkb.core.extensions.loadImageWithGlide
import com.geektechkb.feature_main.R
import com.geektechkb.feature_main.databinding.FragmentPhotoPreviewBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream
import javax.inject.Inject


@AndroidEntryPoint
class PhotoReviewFragment :
    BaseFragment<FragmentPhotoPreviewBinding, PhotoReviewViewModel>(R.layout.fragment_photo_preview) {

    override val binding by viewBinding(FragmentPhotoPreviewBinding::bind)
    override val viewModel by viewModels<PhotoReviewViewModel>()
    private var uri: Uri = Uri.EMPTY
    private val args by navArgs<PhotoReviewFragmentArgs>()

    @Inject
    lateinit var usersPreferencesHelper: UserPreferencesHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        uri = Uri.parse(args.photo)
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
            viewModel.sendMessage(
                usersPreferencesHelper.currentUserPhoneNumber,
                args.phoneNumber,
                "",
                uri.toString(),
                formatCurrentUserTime(Constants.YEAR_MONTH_DAY_HOURS_MINUTES_SECONDS_DATE_FORMAT),
                generateRandomId()
            )
            findNavController().navigate(
                PhotoReviewFragmentDirections.actionPhotoReviewFragmentToChatFragment(
                    args.phoneNumber, args.photo
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