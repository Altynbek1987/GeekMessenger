package com.geektechkb.feature_main.presentation.ui.utils

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.DisplayMetrics
import com.geektechkb.core.extensions.generateRandomId
import com.squareup.picasso.Transformation

class MirrorTransformation : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.preScale(-1f, 1f)

        val mirroredBitmap =
            Bitmap.createBitmap(source, 0, 0, source.width, source.height, matrix, false)
        mirroredBitmap.density = DisplayMetrics.DENSITY_DEFAULT
        source.recycle()
        return mirroredBitmap
    }

    override fun key() = generateRandomId()
}