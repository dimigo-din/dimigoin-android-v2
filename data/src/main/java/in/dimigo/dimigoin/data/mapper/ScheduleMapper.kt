package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.Schedule
import `in`.dimigo.dimigoin.domain.entity.schedule.ScheduleType
import biweekly.Biweekly
import biweekly.util.ICalDate
import java.io.InputStream
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun WeeklyTimetableResponseModel.Timetable.toEntity() = DailyTimetable(
    sequence,
    LocalDate.parse(date)
)

fun InputStream.toSchedulesWithType(type: ScheduleType) =
    Biweekly.parse(this).all().map { cal ->
        cal.events.map { event ->
            repeatScheduleInDateRange(
                type,
                event.description.value,
                event.dateStart.value.toLocalDate()..event.dateEnd.value.toLocalDate()
            )
        }.flatten()
    }.flatten()

private fun ICalDate.toLocalDate() = LocalDate.of(rawComponents.year, rawComponents.month, rawComponents.date)

private operator fun LocalDate.rangeTo(that: LocalDate) = LocalDateRange(this, that)

private class LocalDateRange(
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

private fun repeatScheduleInDateRange(
    type: ScheduleType,
    name: String,
    range: LocalDateRange,
): List<Schedule> =
    range.map {
        Schedule(type, it, name)
    }
