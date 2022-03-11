package `in`.dimigo.dimigoin.data.model.place

import `in`.dimigo.dimigoin.domain.entity.schedule.ScheduleType

data class LocalScheduleModel(
    val type: ScheduleType,
    val date: String,
    val name: String,
)
