package com.geektechkb.core.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

fun ImageView.loadImageWithGlide(url: Any?) = Glide.with(this).load(url).into(this)


fun ImageView.setImage(uri: String) = Glide.with(this).load(uri)
    .circleCrop().transition(DrawableTransitionOptions.withCrossFade()).into(this)


fun ImageView.changeIconWhenActivated(vararg imageViews: ImageView) {
    setOnClickListener {
        isActivated = !isActivated
    }
    imageViews.forEach { imageView ->
        imageView.setOnClickListener {
            imageView.isActivated = !imageView.isActivated
        }
    }
}