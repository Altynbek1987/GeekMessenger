package com.geektechkb.core.extensions.extensions

import com.google.android.material.textfield.TextInputEditText


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