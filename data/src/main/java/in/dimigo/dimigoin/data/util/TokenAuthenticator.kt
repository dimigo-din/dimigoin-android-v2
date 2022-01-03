package `in`.dimigo.dimigoin.data.util

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import retrofit2.await

class TokenAuthenticator(
    private val localSharedPreferenceManager: LocalSharedPreferenceManager,
) : Authenticator {

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
            val authModel = dimigoinService.refreshToken("Bearer $refreshToken").await()
            localSharedPreferenceManager.accessToken = authModel.accessToken
            localSharedPreferenceManager.refreshToken = authModel.refreshToken
            return authModel.accessToken
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    companion object {
        lateinit var dimigoinService: DimigoinApiService
        const val AUTHORIZATION_HEADER = "Authorization"
        private const val ALLOW_RETRY_REFRESH_SECONDS = 60
    }
}