package com.geektechkb.core.extensions

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.switchmaterial.SwitchMaterial
import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage
import kotlin.random.Random

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

fun AvatarView.loadImageAndSetInitialsIfFailed(
    url: String?,
    name: String?,
    lastName: String? = null,
    progressBar: ProgressBar
) {
    loadImage(data = url, onError = { _, _ ->

        val random = Random
        val randomAvatarBackgroundColor =
            Color.rgb(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
            )
        avatarInitialsBackgroundColor = randomAvatarBackgroundColor
        avatarInitials = SpannableStringBuilder(
            name.takeFirstCharacterAndCapitalizeIt()
        ).append(lastName.takeFirstCharacterAndCapitalizeIt()).toString()
    }, onStart = {
        progressBar.isVisible = true
    }, onComplete = {
        progressBar.isVisible = false
    })
}

fun AvatarView.loadImageAndSetInitialsIfFailed(
    url: String?,
    name: String?,
    progressBar: ProgressBar
) {
    loadImage(data = url, onError = { _, _ ->

        val random = Random
        val randomAvatarBackgroundColor =
            Color.rgb(
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255)
            )
        avatarInitialsBackgroundColor = randomAvatarBackgroundColor
        avatarInitials = SpannableStringBuilder(
            name.takeFirstCharacterAndCapitalizeIt()
        ).toString()
    }, onStart = {
        progressBar.isVisible = true
    }, onComplete = {
        progressBar.isVisible = false
    })
}