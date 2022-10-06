package com.geektechkb.feature_main.presentation.ui.fragments.crop

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ViewFlipper
import com.avito.android.krop.KropView
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target

class CropPhotoTarget(
    private val cropContainer: ViewFlipper,
    private val kropView: KropView,
    private val resetZoom: Boolean
) : Target {

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        cropLoading()
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        cropError()
    }

    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        if (bitmap == null) {
            cropError()
        } else {
            kropView.setBitmap(bitmap)
            if (resetZoom) {
                kropView.setZoom(1.0f)
            }
            cropLoaded()
        }
    }

    private fun cropLoading() {
        cropContainer.displayedChild = 0
    }

    private fun cropLoaded() {
        cropContainer.displayedChild = 1
    }

    private fun cropError() {
        cropContainer.displayedChild = 2
    }
}