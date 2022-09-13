package com.geektechkb.core.extensions

fun String.removeExtraSpaces() = replace(" ", "")
fun String?.takeFirstCharacterAndCapitalizeIt() =
    this?.first()?.uppercaseChar()?.toString().toString()