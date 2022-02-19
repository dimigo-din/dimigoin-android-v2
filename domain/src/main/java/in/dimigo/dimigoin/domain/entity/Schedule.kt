package `in`.dimigo.dimigoin.domain.entity

import java.time.LocalDate
import java.time.YearMonth

typealias AnnualSchedule = Map<YearMonth, MonthlySchedule>
typealias MonthlySchedule = List<Schedule>

data class Schedule(
    val type: ScheduleType,
    val date: LocalDate,
    val name: String,
)

enum class ScheduleType(val color: Long) {
    EXAM(0xFFB332D3),
    EVENT(0xFFE9B839),
    HOME(0xFF60C944),
    JAIL(0xFFF66A4B),
    VACATION(0xFF4B90F6);
}
