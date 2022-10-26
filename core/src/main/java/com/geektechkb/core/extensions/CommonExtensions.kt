package com.geektechkb.core.extensions

import java.text.SimpleDateFormat
import java.util.*

fun formatCurrentUserTime(dateFormat: String): String =
    SimpleDateFormat(dateFormat, Locale.getDefault()).format(Date())

fun generateRandomId() = UUID.randomUUID().toString().substring(0, 15)
fun parseToFormat(
    date: String,
    patternToParseTo: String = "HH:mm",
    patternToParseFrom: String = "yyyy-MM-dd-HH:mm:ss"
): String? {
    val parser = SimpleDateFormat(patternToParseFrom, Locale.forLanguageTag("RU"))
    val formatter = SimpleDateFormat(patternToParseTo, Locale.forLanguageTag("RU"))
    return parser.parse(date)?.let { formatter.format(it) }
}