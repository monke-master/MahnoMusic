package com.monke.machnomusic3.data.extensions

fun Int.seconds() = this / 1000

fun Int.milliseconds() = this*1000

fun Int.formatDuration(): String {
    val totalSeconds = this / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    if (seconds < 10)
        return "$minutes:0$seconds"
    return "$minutes:$seconds"
}