package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.datasource.SchoolScheduleDataSource
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.mapper.toSchedulesWithType
import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.schedule.AnnualSchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable
import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class ScheduleRepositoryImpl(
    private val dimigoinService: DimigoinApiService,
    private val scheduleDataSource: SchoolScheduleDataSource,
    private val sharedPreferenceManager: LocalSharedPreferenceManager,
) : ScheduleRepository {

    private var weeklyTimetable: WeeklyTimetable? = null
    private var schoolSchedules: AnnualSchedule? = null

    private val scheduleMutex = Mutex()

    override suspend fun getTimetableByClass(grade: Int, `class`: Int): Result<WeeklyTimetable> =
        resultFromCall(
            dimigoinService.getTimetable(grade, `class`),
            cached = weeklyTimetable
        ) { response ->
            val weeklyTimetable =
                response.timetable.map(WeeklyTimetableResponseModel.Timetable::toEntity)

            val monday = getFirstDayOfWeek(weeklyTimetable.firstOrNull()?.date ?: LocalDate.now())

            List(5) { days ->
                monday.plusDays(days.toLong())
            }.map {
                weeklyTimetable.find { timetable -> timetable.date == it } ?: DailyTimetable(
                    emptyList(),
                    it
                )
            }
        }

    private fun getFirstDayOfWeek(date: LocalDate) = date.with(DayOfWeek.MONDAY)

    override suspend fun getSchoolSchedule(): Result<AnnualSchedule> {
        return getSchoolScheduleFromLocal().recoverCatching {
            Log.d(TAG, "getSchoolSchedule: schedule not found in local")
            getSchoolScheduleFromRemote().getOrThrow()
        }
    }

    private suspend fun getSchoolScheduleFromLocal(): Result<AnnualSchedule> {
        return scheduleMutex.withLock {
            sharedPreferenceManager.schedules?.let {
                Log.d(TAG, "getSchoolScheduleFromLocal: schedule found in local: $it")
                it.groupBy { schedule -> YearMonth.from(schedule.date) }
            }?.let {
                Result.success(it)
            } ?: Result.failure(NoSuchElementException("Schedules are not stored in local"))
        }
    }

    private suspend fun getSchoolScheduleFromRemote(): Result<AnnualSchedule> = try {
        withContext(Dispatchers.IO) {
            Result.success(
                SchoolScheduleDataSource.Calendar.values().associateBy {
                    it.type
                }.mapValues {
                    scheduleDataSource.getICalStream(it.value)
                }.map {
                    it.value.toSchedulesWithType(it.key)
                }.flatten().also {
                    Log.d(TAG, "getSchoolScheduleFromRemote: storing schedule $it")
                    sharedPreferenceManager.schedules = it
                }.groupBy {
                    YearMonth.from(it.date)
                }.also {
                    schoolSchedules = it
                }
            )
        }
    } catch (e: Exception) {
        Result.failure(e)
    }

    companion object {
        private const val TAG = "ScheduleRepositoryImpl"
    }
}
