package `in`.dimigo.dimigoin.ui.util

import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.WeekFields

fun LocalTime.asKorean12HoursString() = (if (isBefore(NOON)) "오전 " else "오후 ") +
    (hour % 12).let { if (it == 0) 12 else it }.toString() +
    ":" +
    "$minute".padStart(2, '0')

private val NOON = LocalTime.of(12, 0)

fun LocalDate.asKoreanWeekString(): String {
    fun Int.toKorean(): String = listOf("", "첫", "둘", "셋", "넷", "다섯")[this]

    val centerDayOfWeek = this.plusDays(4L - this.dayOfWeek.value)
    val firstDayOfMonth = centerDayOfWeek.withDayOfMonth(1)
    val centerDayOfFirstWeekOfMonth = firstDayOfMonth.plusDays(4L - firstDayOfMonth.dayOfWeek.value)
    val month = centerDayOfWeek.monthValue
    val currentWeek = centerDayOfWeek.get(WeekFields.ISO.weekOfYear())
    val firstWeekOfMonth = firstDayOfMonth.get(WeekFields.ISO.weekOfYear())
    val weekOfCenterDayOfFirstWeekOfMonth = centerDayOfFirstWeekOfMonth.get(WeekFields.ISO.weekOfYear())

    val weekOfMonth = currentWeek - firstWeekOfMonth + if (weekOfCenterDayOfFirstWeekOfMonth == firstWeekOfMonth) { 1 } else { 0 }

    return "${month}월 ${weekOfMonth.toKorean()}째 주"
}

private const val TAG = "DateTimeUtil"
