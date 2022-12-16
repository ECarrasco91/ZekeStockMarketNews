package com.ezequielc.zekestockmarketnews.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.util.Date

fun convertToTimestamp(date: String) = Instant.parse(date).epochSecond

fun formatTimestamp(pattern: String, value: Long): String {
    val simpleDateFormat = SimpleDateFormat(pattern)
    val date = Date(value * 1000)
    return simpleDateFormat.format(date)
}

