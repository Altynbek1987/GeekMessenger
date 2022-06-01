package com.geektechkb.core.extensions.extensions

import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import com.geektechkb.core.R
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.deleteASingleCharacterInEditText() {
    val cursorPosition = selectionStart
    if (cursorPosition > 0)
        text?.delete(cursorPosition - 1, cursorPosition)
}

fun TextInputEditText.requestFocusOnTheNextDigit(editTextToRequestUserFocusOn: TextInputEditText) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (text?.length == 1)
                editTextToRequestUserFocusOn.requestFocus()
        }

        override fun afterTextChanged(s: Editable?) {
        }
    })
}


fun TextInputEditText.retrieveInputVerificationCode(
    secondDigit: TextInputEditText,
    thirdDigit: TextInputEditText,
    fourthDigit: TextInputEditText,
    fifthDigit: TextInputEditText,
    sixthDigit: TextInputEditText,
) =
    text.toString() + secondDigit.text + thirdDigit.text + fourthDigit.text + fifthDigit.text + sixthDigit.text


fun TextInputEditText.disableKeyListeners(
    secondEditText: TextInputEditText,
    thirdEditText: TextInputEditText,
    fourthEditText: TextInputEditText,
    fifthEditText: TextInputEditText,
    sixthEditText: TextInputEditText,
) {
    keyListener = null
    secondEditText.keyListener = null
    thirdEditText.keyListener = null
    fourthEditText.keyListener = null
    fifthEditText.keyListener = null
    sixthEditText.keyListener = null
}