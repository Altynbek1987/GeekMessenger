package com.geektechkb.core.extensions.extensions

import android.view.View
import com.google.android.material.textfield.TextInputEditText

fun View.appendTextDependingOnTheFocus(
    firstEditText: TextInputEditText,
    secondEditText: TextInputEditText,
    thirdEditText: TextInputEditText,
    fourthEditText: TextInputEditText,
    fifthEditText: TextInputEditText,
    sixthEditText: TextInputEditText,
    textToAppend: CharSequence
) {
    when (rootView.findFocus()) {
        firstEditText -> firstEditText.text?.append(textToAppend)
        secondEditText -> secondEditText.text?.append(textToAppend)
        thirdEditText -> thirdEditText.text?.append(textToAppend)
        fourthEditText -> fourthEditText.text?.append(textToAppend)
        fifthEditText -> fifthEditText.text?.append(textToAppend)
        sixthEditText -> sixthEditText.text?.append(textToAppend)
    }
}

fun View.deleteACharacterDependingOnFocus(
    firstEditText: TextInputEditText,
    secondEditText: TextInputEditText,
    thirdEditText: TextInputEditText,
    fourthEditText: TextInputEditText,
    fifthEditText: TextInputEditText,
    sixthEditText: TextInputEditText,

    ) {
    when (rootView.findFocus()) {
        firstEditText -> {
            firstEditText.text?.clear()
        }
        secondEditText -> {

            secondEditText.text?.clear()
            firstEditText.requestFocus()
        }
        thirdEditText -> {
            thirdEditText.text?.clear()
            secondEditText.requestFocus()
        }
        fourthEditText -> {
            fourthEditText.text?.clear()
            thirdEditText.requestFocus()
        }
        fifthEditText -> {
            fifthEditText.text?.clear()
            fourthEditText.requestFocus()
        }
        sixthEditText -> {
            sixthEditText.text?.clear()
            fifthEditText.requestFocus()
        }
    }
}