package com.geektechkb.core.extensions

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText

fun TextInputEditText.addTextChangedListenerAnonymously(
    doSomethingOnTextChanged: ((() -> Unit))? = null,
    doSomethingBeforeTextChanged: ((() -> Unit))? = null,
    doSomethingAfterTextChanged: ((() -> Unit))? = null
) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            doSomethingBeforeTextChanged?.invoke()
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            doSomethingOnTextChanged?.invoke()
        }

        override fun afterTextChanged(s: Editable?) {
            doSomethingAfterTextChanged?.invoke()
        }

    })
}