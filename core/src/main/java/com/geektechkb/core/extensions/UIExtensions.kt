package com.geektechkb.core.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.firebase.storage.StorageReference

fun ImageView.loadImageWithGlide(url: Any?) = Glide.with(this).load(url).into(this)
fun ImageView.loadImageFromFirebaseCloudStorageReference(cloudStorageReference: StorageReference) =
    Glide.with(this).load(cloudStorageReference).into(this)

fun ImageView.setImage(uri: String) {
    Glide.with(this)
        .load(uri)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}
