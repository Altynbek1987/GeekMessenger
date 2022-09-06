package com.geektechkb.core.extensions

import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.OutputStream

fun ImageView.setImage(uri: String?) {
    Glide.with(this)
        .load(uri)
        .circleCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}

fun ImageView.setImagee(uri: Uri?) {
    Glide.with(this)
        .load(uri)
        .centerCrop()
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}


fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}

fun Fragment.showShortDurationSnackbar(text: CharSequence) {
    view?.let { Snackbar.make(it, text, Snackbar.LENGTH_SHORT).show() }
}

fun Fragment.showLongDurationSnackbar(text: CharSequence) {
    view?.let { Snackbar.make(it, text, Snackbar.LENGTH_LONG).show() }
}


fun Fragment.checkForPermissionStatusAndRequestIt(
    requestPermissionLauncher: ActivityResultLauncher<String>,
    permission: String,
): Boolean {
    return when (ContextCompat.checkSelfPermission(
        requireContext(),
        permission
    ) == PackageManager.PERMISSION_GRANTED) {
        true -> true

        else -> {
            requestPermissionLauncher.launch(permission)
            false
        }
    }
}

fun Fragment.checkForMultiplePermissionsAndRequestThem(
    requestPermissionLauncher: ActivityResultLauncher<Array<String>>,
    permission: Array<String>,
): Boolean {
    for (per in permission) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                per
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(permission)
            return false
        }
    }
    return true
}

fun Fragment.createRequestPermissionLauncherToRequestSinglePermission(
    permission: String,
    actionWhenPermissionHasBeenGranted: (() -> Unit)? = null,
    actionWhenPermissionHasBeenDenied: (() -> Unit)? = null
): ActivityResultLauncher<String> {
    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isPermissionGranted ->
            when {
                isPermissionGranted -> actionWhenPermissionHasBeenGranted?.invoke()
                !shouldShowRequestPermissionRationale(permission) -> actionWhenPermissionHasBeenDenied?.invoke()
                    ?: ""
            }


        }
    return requestPermissionLauncher
}


fun Fragment.hideSoftKeyboard() {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
}

fun Fragment.checkWhetherSoftKeyboardIsOpenedOrNot(): Boolean {
    val inputMethodManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    return inputMethodManager.isAcceptingText

}

fun Fragment.overrideOnBackPressed(actionWhenBackButtonPressed: () -> Unit) {
    activity?.onBackPressedDispatcher?.addCallback(
        requireActivity(),
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                actionWhenBackButtonPressed.invoke()
            }

        })
}

fun Fragment.stateBottomSheet(bottomSheet: BottomSheetBehavior<MaterialCardView>?, state: Int) {
    val metrics = resources.displayMetrics
    bottomSheet?.peekHeight = metrics.heightPixels / 2
    bottomSheet?.state = state
    bottomSheet?.maxWidth = 2000
}

fun Fragment.loadWithPicassoCenterCropImageIntoTargetAndDoSomethingWithBitmap(
    url: String,
    actionOnBitmapLoaded: ((Bitmap?) -> Unit)? = null,
    actionOnBitmapFailed: ((Exception?) -> Unit)? = null,
    actionOnPrepareLoad: ((Drawable?) -> Unit)? = null
) =
    Picasso.get().load(url).centerCrop().resize(
        getDisplayWidthPixels(),
        getDisplayHeightPixels()
    ).into(object : Target {

        override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
            actionOnBitmapLoaded?.invoke(bitmap)
        }

        override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
            actionOnBitmapFailed?.invoke(e)
        }

        override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
            actionOnPrepareLoad?.invoke(placeHolderDrawable)
        }

    })

private fun Fragment.getDisplayWidthPixels() = requireContext().resources.displayMetrics.widthPixels
private fun Fragment.getDisplayHeightPixels() =
    requireContext().resources.displayMetrics.heightPixels


fun Fragment.openGalleryBottomSheet(
    cardView: MaterialCardView,
    bottomSheetBehavior: BottomSheetBehavior<MaterialCardView>?,
    appBarLayout: AppBarLayout,
    coordinatorLayout: CoordinatorLayout,
    actionOnDialogOpened: (() -> Unit)? = null,
    actionOnDialogHalfExpanded: (() -> Unit)? = null,
    actionOnDialogStateExpanded: (() -> Unit)? = null,
    actionOnDialogStateDragging: (() -> Unit)? = null,
    actionOnDialogStateHidden: (() -> Unit)? = null,
    actionOnDialogAnyState: (() -> Unit)? = null
) {
    actionOnDialogOpened?.invoke()
    coordinatorLayout.isVisible = true
    stateBottomSheet(bottomSheetBehavior, BottomSheetBehavior.STATE_HALF_EXPANDED)
    bottomSheetBehavior?.addBottomSheetCallback(object :
        BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(bottomSheet: View, newState: Int) {
            when (newState) {
                BottomSheetBehavior.STATE_HALF_EXPANDED ->
                    actionOnDialogHalfExpanded?.invoke()

                BottomSheetBehavior.STATE_EXPANDED -> {
                    showView(appBarLayout, getActionBarSize(), appBarLayout)
                    actionOnDialogStateExpanded?.invoke()
                    cardView.radius =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            0F,
                            context?.resources?.displayMetrics
                        )
                }
                BottomSheetBehavior.STATE_HIDDEN ->
                    actionOnDialogStateHidden?.invoke()
                BottomSheetBehavior.STATE_DRAGGING -> actionOnDialogStateDragging?.invoke()
                else -> {
                    actionOnDialogAnyState?.invoke()
                    cardView.radius =
                        TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_DIP,
                            10F,
                            context?.resources?.displayMetrics
                        )
                    hideAppBar(appBarLayout, appBarLayout)
                }
            }
        }

        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }
    })
}

fun Fragment.showView(view: View, size: Int, appBarLayout: AppBarLayout) {
    val params = view.layoutParams
    params.height = size
    appBarLayout.isVisible = true
    view.layoutParams = params
}


fun Fragment.hideAppBar(view: View, appBarLayout: AppBarLayout) {
    val params = view.layoutParams
    params.height = 4
    appBarLayout.isVisible = false
    view.layoutParams = params
}

fun Fragment.getActionBarSize(): Int {
    val array =
        requireContext().theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
    return array.getDimension(0, 0f).toInt()
}

fun Fragment.saveImageToLocalStorageAndRetrieveItsUri(bitmap: Bitmap): Uri {
    val filename = "IMG_${System.currentTimeMillis()}.png"
    var outputStream: OutputStream?
    var imageUri: Uri?
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        put(MediaStore.Video.Media.IS_PENDING, 1)
    }

    val contentResolver = requireActivity().applicationContext.contentResolver

    contentResolver.also { resolver ->
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        outputStream = imageUri?.let { resolver.openOutputStream(it) }
    }

    outputStream?.use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }

    contentValues.clear()
    contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
    imageUri?.let {
        contentResolver.update(it, contentValues, null, null)
    }
    return imageUri!!
}