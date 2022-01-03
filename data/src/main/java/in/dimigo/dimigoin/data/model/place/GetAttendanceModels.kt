package `in`.dimigo.dimigoin.data.model.place

data class GetAttendanceResponseModel(
    val logs: List<AttendanceResponseModel>,
)

data class AttendanceResponseModel(
    val place: PlaceResponseModel,
    val remark: String?,
)
