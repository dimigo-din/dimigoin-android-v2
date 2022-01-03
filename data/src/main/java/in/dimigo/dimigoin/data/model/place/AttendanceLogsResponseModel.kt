package `in`.dimigo.dimigoin.data.model.place

data class AttendanceLogsResponseModel(
    val logs: List<AttendanceLogResponseModel>,
)

data class AttendanceLogResponseModel(
    val remark: String?,
    val place: PlaceResponseModel,
)
