package com.geektechkb.core.extensions.extensions

import android.view.View
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText

fun TextView.setOnNumericClickListener(
    view: View?,
    firstDigit: TextInputEditText,
    secondDigit: TextInputEditText,
    thirdDigit: TextInputEditText,
    fourthDigit: TextInputEditText,
    fifthDigit: TextInputEditText,
    sixthDigit: TextInputEditText
) {
    setOnClickListener {
        (view?.appendTextDependingOnTheFocus(
            firstDigit,
            secondDigit,
            thirdDigit,
            fourthDigit,
            fifthDigit,
            sixthDigit,
            text.toString()
        ))

    }
}

fun TextView.setOnNumericClickListener(editText: TextInputEditText) {
    setOnClickListener {
        editText.append(text)
    }


}