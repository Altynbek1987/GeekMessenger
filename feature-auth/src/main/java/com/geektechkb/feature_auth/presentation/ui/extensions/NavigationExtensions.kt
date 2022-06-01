package com.geektechkb.feature_auth.presentation.ui.extensions

import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.geektechkb.feature_auth.R

fun Fragment.mainNavController() =
    requireActivity().findNavController(R.id.nav_host_fragment_container_auth)

fun ImageView.setImage(uri: String) {
    Glide.with(this)
        .load(uri)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}