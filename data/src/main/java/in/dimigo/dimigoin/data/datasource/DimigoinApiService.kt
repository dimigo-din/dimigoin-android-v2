package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.data.model.place.GetAttendanceResponseModel
import `in`.dimigo.dimigoin.data.model.place.GetPlacesResponseModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceRequestModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceResponseModel
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.model.user.LoginResponseModel
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface DimigoinApiService {

    @POST("/auth")
    fun login(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>

    @GET("/place")
    fun getPlaces(): Call<GetPlacesResponseModel>

    @GET("/attendance")
    fun getAttendanceLogs(): Call<GetAttendanceResponseModel>

    @POST("/attendance")
    fun addAttendanceLog(@Body attendanceLogRequestModel: PostAttendanceRequestModel): Call<PostAttendanceResponseModel>
}
