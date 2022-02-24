package `in`.dimigo.dimigoin.domain.entity.schedule

import java.time.LocalDate

typealias WeeklyTimetable = List<DailyTimetable>

data class DailyTimetable(
    val sequence: List<String?>,
    val date: LocalDate,
)
