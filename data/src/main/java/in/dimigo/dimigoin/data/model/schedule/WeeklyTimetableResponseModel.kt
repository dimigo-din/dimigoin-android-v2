package `in`.dimigo.dimigoin.data.model.schedule

data class WeeklyTimetableResponseModel(
    val timetable: List<Timetable>
) {
    data class Timetable(
        val sequence: List<String?>,
        val date: String,
    )
}
