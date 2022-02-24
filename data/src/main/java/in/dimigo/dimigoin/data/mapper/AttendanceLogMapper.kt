package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import com.google.gson.GsonBuilder

fun String.toAttendanceLog(): AttendanceLog {
    return gson.fromJson(this, AttendanceLog::class.java)
}

fun AttendanceLog.toJsonString(): String {
    return gson.toJson(this)
}

private val gson = GsonBuilder().create()
