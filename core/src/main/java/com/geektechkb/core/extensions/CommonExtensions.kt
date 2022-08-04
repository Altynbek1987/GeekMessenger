package com.geektechkb.core.extensions

import java.text.SimpleDateFormat
import java.util.*

fun formatCurrentUserTime(dateFormat: String): String =
    SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date())

fun generateRandomId() = UUID.randomUUID().toString().substring(0, 15)

