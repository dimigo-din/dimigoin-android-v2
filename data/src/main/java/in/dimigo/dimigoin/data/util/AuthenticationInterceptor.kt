package `in`.dimigo.dimigoin.data.util

import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor(
    private val localSharedPreferenceManager: LocalSharedPreferenceManager,
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .apply {
                val accessToken = localSharedPreferenceManager.accessToken
                if (accessToken != null) {
                    addHeader("Authorization", "Bearer $accessToken")
                    Log.d(TAG, "intercept: $accessToken")
                }
            }
            .build().let { chain.proceed(it) }
    }

    companion object {
        private const val TAG = "AuthenticationIntercept"
    }
}
