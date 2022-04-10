package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.data.model.meal.MealSequenceResponseModel
import `in`.dimigo.dimigoin.data.model.meal.MealTimeResponseModel
import `in`.dimigo.dimigoin.data.model.meal.TodayMealResponseModel
import `in`.dimigo.dimigoin.data.model.meal.WeeklyMealResponseModel
import `in`.dimigo.dimigoin.data.model.place.GetAttendanceResponseModel
import `in`.dimigo.dimigoin.data.model.place.GetPlacesResponseModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceRequestModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceResponseModel
import `in`.dimigo.dimigoin.data.model.schedule.WeeklyTimetableResponseModel
import `in`.dimigo.dimigoin.data.model.user.GetUserMeResponseModel
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.model.user.LoginResponseModel
import retrofit2.Call
import retrofit2.http.*

interface DimigoinApiService {

    @POST("/auth")
    fun login(@Body loginRequestModel: LoginRequestModel): Call<LoginResponseModel>

    @POST("/auth/refresh")
    fun refreshToken(@Header("Authorization") refreshToken: String): Call<LoginResponseModel>

    @GET("/user/me")
    fun getMyIdentity(): Call<GetUserMeResponseModel>

    @GET("/place")
    fun getPlaces(): Call<GetPlacesResponseModel>

    @GET("/attendance")
    fun getAttendanceLogs(): Call<GetAttendanceResponseModel>

    @POST("/attendance")
    fun addAttendanceLog(@Body attendanceLogRequestModel: PostAttendanceRequestModel): Call<PostAttendanceResponseModel>

    @GET("/meal/today")
    fun getTodayMeal(): Call<TodayMealResponseModel>

    @GET("/meal/weekly")
    fun getWeeklyMeal(): Call<WeeklyMealResponseModel>

    @GET("/dalgeurak/sequence")
    fun getMealSequence(): Call<MealSequenceResponseModel>

    @GET("/dalgeurak/time")
    fun getMealTimes(): Call<MealTimeResponseModel>

    @GET("/timetable/weekly/grade/{grade}/class/{class}")
    fun getTimetable(
        @Path("grade") grade: Int,
        @Path("class") `class`: Int
    ): Call<WeeklyTimetableResponseModel>
}
