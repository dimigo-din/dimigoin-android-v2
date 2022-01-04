package `in`.dimigo.dimigoin.domain.entity

import java.util.UUID

data class AttendanceLog(
    val placeId: String,
    val remark: String?,
    val _id: String = UUID.randomUUID().toString(),
)
