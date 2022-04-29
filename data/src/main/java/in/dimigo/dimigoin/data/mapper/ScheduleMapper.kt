package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.place.LocalScheduleModel
import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.data.util.gson
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.Schedule
import `in`.dimigo.dimigoin.domain.entity.schedule.ScheduleType
import biweekly.Biweekly
import biweekly.util.ICalDate
import java.io.InputStream
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun String.toSchedule(): Schedule =
    gson.fromJson(this, LocalScheduleModel::class.java).toEntity()

fun Schedule.toJsonString(): String =
    gson.toJson(this.toLocalModel())

fun Schedule.toLocalModel(): LocalScheduleModel =
    LocalScheduleModel(type, date.toString(), name)

fun LocalScheduleModel.toEntity(): Schedule =
    Schedule(type, LocalDate.parse(date), name)

fun WeeklyTimetableResponseModel.Timetable.toEntity() = DailyTimetable(
    sequence,
    LocalDate.parse(date)
)

fun InputStream.toSchedulesWithType(type: ScheduleType) =
    Biweekly.parse(this).all().map { cal ->
        cal.events.map { event ->
            repeatScheduleInDateRange(
                type,
                event.summary.value,
                event.dateStart.value.toLocalDate()..event.dateEnd.value.toLocalDate()
            )
        }.flatten()
    }.flatten()

private fun ICalDate.toLocalDate() =
    LocalDate.of(rawComponents.year, rawComponents.month, rawComponents.date)

private operator fun LocalDate.rangeTo(that: LocalDate) = LocalDateRange(this, that)

internal data class LocalDateRange(
    private val startInclusive: LocalDate,
    private val endExclusive: LocalDate,
) : Iterable<LocalDate> {
    override fun iterator(): Iterator<LocalDate> = LocalDateIterator(this)

    class LocalDateIterator(
        val range: LocalDateRange,
    ) : Iterator<LocalDate> {

        var index = 0L
        val size = ChronoUnit.DAYS.between(range.startInclusive, range.endExclusive)

        override fun hasNext(): Boolean = index < size

        override fun next(): LocalDate = range.startInclusive.plusDays(index++)
    }
}

internal fun repeatScheduleInDateRange(
    type: ScheduleType,
    name: String,
    range: LocalDateRange,
): List<Schedule> =
    range.map {
        Schedule(type, it, name)
    }
