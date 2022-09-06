package com.geektechkb.core.extensions

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.switchmaterial.SwitchMaterial

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


fun SwitchMaterial.actionOnCheckedChange(action: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        action(isChecked)
    }
}