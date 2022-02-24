package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.domain.entity.schedule.ScheduleType
import java.io.InputStream
import java.lang.IllegalStateException
import okhttp3.OkHttpClient
import okhttp3.Request

class SchoolScheduleDataSource(
    private val client: OkHttpClient
) {
    fun getICalStream(type: Calendar): InputStream {
        val req = Request.Builder().url(ICAL_URL + type.token).build()
        val res = client.newCall(req).execute()

        return res.body?.byteStream() ?: throw IllegalStateException("Failed to get iCal")
    }

    enum class Calendar(val token: String, val type: ScheduleType) {
        EXAM("aAAxADI3NmFiODBlY2MwNDMyYzg0NGY0NWU5ODIyODI5MGVjNjBmMjNhZTk", ScheduleType.EXAM),
        EVENT("aAAxAGMyOWRmYjBjYWIxMzQyMDdkM2ZmZTlmNWM5ZWQyMjVkZGI4Y2M0OTQ", ScheduleType.EVENT),
        HOME("aAAxAGY2ZDk2MmI2M2YxNGViOTg5YzYwYmMxODM3MTQ2MmY5YzgxY2VjMzc", ScheduleType.HOME),
        JAIL("aAAxADNiN2E3YWRhYTZkOGYzM2I0OGE2YTA1MWU0OWYxMjA0OTIwOWZlYWQ", ScheduleType.JAIL),
        VACATION("aAAxADUyNmRhODdjNjg3M2Y2YWQ3ZTFlNmRhN2MyNzIzZjEwZjMzYzIyYmI", ScheduleType.VACATION);
    }

    companion object {
        const val ICAL_URL = "https://api.band.us/ical?token="
    }
}
