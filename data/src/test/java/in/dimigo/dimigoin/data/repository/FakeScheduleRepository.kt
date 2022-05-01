package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.domain.entity.schedule.AnnualSchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable
import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository
import java.time.LocalDate

class FakeScheduleRepository : ScheduleRepository {
    override suspend fun getTimetableByClass(grade: Int, `class`: Int): Result<WeeklyTimetable> {
        return Result.success(TIMETABLE)
    }

    override suspend fun getSchoolSchedule(): Result<AnnualSchedule> {
        return Result.success(mapOf())
    }

    companion object {
        val TIMETABLE = List(5) {
            DailyTimetable(
                listOf("국어", "수학", "영어", "화학I", "물리I", "플밍", "동아리"),
                LocalDate.of(2022, 5, 2).plusDays(it.toLong())
            )
        }
    }
}