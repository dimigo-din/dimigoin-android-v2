package `in`.dimigo.dimigoin.data.datasource

import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.model.user.LoginResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DimigoinApiService {

    @POST("/auth")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<LoginResponseModel>
}
