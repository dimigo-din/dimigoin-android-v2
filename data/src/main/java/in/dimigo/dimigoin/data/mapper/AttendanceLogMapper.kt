package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.util.gson
import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog

fun String.toAttendanceLog(): AttendanceLog {
    return gson.fromJson(this, AttendanceLog::class.java)
}

fun AttendanceLog.toJsonString(): String {
    return gson.toJson(this)
}
