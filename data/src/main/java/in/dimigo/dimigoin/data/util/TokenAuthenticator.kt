package `in`.dimigo.dimigoin.data.util

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class TokenAuthenticator(
    private val localSharedPreferenceManager: LocalSharedPreferenceManager,
) : Authenticator {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    private val dimigoinApiService = retrofit.create(DimigoinApiService::class.java)

    private var recentRefreshedMillis: Long? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        if (isRecentlyRefreshed()) return null
        val newToken = runBlocking { refreshAndGetAccessToken() } ?: return null

        return response.request.newBuilder()
            .header(AUTHORIZATION_HEADER, "Bearer $newToken")
            .build()
    }

    private fun isRecentlyRefreshed(): Boolean {
        val lastRefreshTemp = recentRefreshedMillis
        val now = System.currentTimeMillis()
        recentRefreshedMillis = now
        lastRefreshTemp ?: return false
        return now - lastRefreshTemp < ALLOW_RETRY_REFRESH_SECONDS * 1000
    }

    private suspend fun refreshAndGetAccessToken(): String? {
        try {
            val refreshToken = localSharedPreferenceManager.refreshToken ?: return null
            val authModel = dimigoinApiService.refreshToken("Bearer $refreshToken").await()
            localSharedPreferenceManager.accessToken = authModel.accessToken
            localSharedPreferenceManager.refreshToken = authModel.refreshToken
            return authModel.accessToken
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        private const val BASE_URL = "https://api.dimigo.in/"
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val ALLOW_RETRY_REFRESH_SECONDS = 60
        private const val TAG = "TokenAuthenticator"
    }
}