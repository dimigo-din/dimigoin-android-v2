package `in`.dimigo.dimigoin.data.model.place

import com.google.gson.annotations.SerializedName

data class PostAttendanceRequestModel(
    @SerializedName("place") val placeId: String,
    val remark: String?,
)

data class PostAttendanceResponseModel(
    val attendanceLog: SimpleAttendanceResponseModel,
)

data class SimpleAttendanceResponseModel(
    @SerializedName("place") val placeId: String,
    val remark: String?,
)
