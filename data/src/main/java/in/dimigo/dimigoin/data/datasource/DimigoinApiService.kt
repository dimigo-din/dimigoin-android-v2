package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.data.model.place.AllPlaceResponseModel
import `in`.dimigo.dimigoin.data.model.place.AttendanceLogRequestModel
import `in`.dimigo.dimigoin.data.model.place.AttendanceLogResponseModel
import `in`.dimigo.dimigoin.data.model.place.AttendanceLogsResponseModel
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.model.user.LoginResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DimigoinApiService {

    @POST("/auth")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<LoginResponseModel>

    @GET("/place")
    suspend fun getPlaces(): Response<AllPlaceResponseModel>

    @GET("/attendance")
    suspend fun getAttendanceLogs(): Response<AttendanceLogsResponseModel>

    @POST("/attendance")
    suspend fun addAttendanceLog(@Body attendanceLogRequestModel: AttendanceLogRequestModel): Response<AttendanceLogResponseModel>
}
