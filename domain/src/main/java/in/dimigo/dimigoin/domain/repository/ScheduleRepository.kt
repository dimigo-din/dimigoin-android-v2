package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.schedule.AnnualSchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable

interface ScheduleRepository {
    suspend fun getTimetableByClass(grade: Int, `class`: Int): Result<WeeklyTimetable>
    suspend fun getSchoolSchedule(): Result<AnnualSchedule>
}
