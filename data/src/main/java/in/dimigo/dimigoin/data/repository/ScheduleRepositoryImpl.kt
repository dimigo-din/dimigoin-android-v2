package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.SchoolScheduleDataSource
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.mapper.toSchedulesWithType
import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.schedule.AnnualSchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable
import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ScheduleRepositoryImpl(
    private val dimigoinService: DimigoinApiService,
    private val scheduleDataSource: SchoolScheduleDataSource,
) : ScheduleRepository {

    var weeklyTimetable: WeeklyTimetable? = null

    override suspend fun getTimetableByClass(grade: Int, `class`: Int): Result<WeeklyTimetable> = resultFromCall(
        dimigoinService.getTimetable(grade, `class`),
        cached = weeklyTimetable
    ) { response ->
        val weeklyTimetable = response.timetable.map(WeeklyTimetableResponseModel.Timetable::toEntity)

        val monday = getFirstDayOfWeek(weeklyTimetable.firstOrNull()?.date ?: LocalDate.now())

        List(5) { days ->
            monday.plusDays(days.toLong())
        }.map {
            weeklyTimetable.find { timetable -> timetable.date == it } ?: DailyTimetable(emptyList(), it)
        }
    }

    private fun getFirstDayOfWeek(date: LocalDate) = date.with(DayOfWeek.MONDAY)

    override suspend fun getSchoolSchedule(): Result<AnnualSchedule> = try {
        withContext(Dispatchers.IO) {
            Result.success(
                SchoolScheduleDataSource.Calendar.values().associateBy {
                    it.type
                }.mapValues {
                    scheduleDataSource.getICalStream(it.value)
                }.map {
                    it.value.toSchedulesWithType(it.key)
                }.flatten().groupBy {
                    YearMonth.from(it.date)
                }
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}
