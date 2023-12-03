package com.monke.machnomusic3.data.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.formatDate(locale: Locale): String {
    val formatter = SimpleDateFormat("dd.MM.yyyy", locale)
    return formatter.format(Date(this))
}