package `in`.dimigo.dimigoin.data.util

import okhttp3.Interceptor
import okhttp3.Response

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.request()
            .newBuilder()
            .apply {
                if (SessionDataStore.accessToken != null)
                    addHeader("Authorization", "Bearer ${SessionDataStore.accessToken}")
            }
            .build().let { chain.proceed(it) }
    }
}
