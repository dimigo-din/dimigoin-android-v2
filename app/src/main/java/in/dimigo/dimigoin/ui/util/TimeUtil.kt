package `in`.dimigo.dimigoin.ui.util

import java.time.LocalTime

fun LocalTime.asKorean12HoursString() = (if (isBefore(NOON)) "오전 " else "오후 ") +
    (hour % 12).let { if (it == 0) 12 else it }.toString() +
    ":" +
    "$minute".padStart(2, '0')

private val NOON = LocalTime.of(12, 0)
