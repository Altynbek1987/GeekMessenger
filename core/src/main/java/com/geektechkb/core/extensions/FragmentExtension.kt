package com.geektechkb.core.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar

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


fun Fragment.stateBottomSheet(bottomSheet: BottomSheetBehavior<MaterialCardView>?, state: Int) {
    val metrics = resources.displayMetrics
    bottomSheet?.peekHeight = metrics.heightPixels / 2
    bottomSheet?.state = state
    bottomSheet?.maxWidth = 2000
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


fun Fragment.checkWhetherPermissionHasBeenGrantedOrNot(
    context: Context,
    permission: String,
    activity: Activity
): Intent {
    val galleryIntent =
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

    when (ContextCompat.checkSelfPermission(context, permission)) {
        -1 -> ActivityCompat.requestPermissions(
            activity, arrayOf(
                permission
            ), 0
        )
    }
    return galleryIntent
}

fun Fragment.hasPermissionCheckAndRequest(
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
