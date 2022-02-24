package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import java.time.LocalDate

fun WeeklyTimetableResponseModel.Timetable.toEntity() = DailyTimetable(
    sequence,
    LocalDate.parse(date)
)
