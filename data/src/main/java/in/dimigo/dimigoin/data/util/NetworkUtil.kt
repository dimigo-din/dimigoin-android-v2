package `in`.dimigo.dimigoin.data.util

import android.os.Debug
import android.util.DebugUtils
import android.util.Log
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import okhttp3.internal.toHexString
import retrofit2.Call
import java.util.*

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun <T, R> resultFromCall(
    call: Call<T>,
    cached: R? = null,
    extraOnFailure: ((errorBody: ResponseBody) -> Unit)? = null,
    mapOnSuccess: ((T) -> R),
): Result<R> {
    val uid = UUID.randomUUID().mostSignificantBits.toHexString().slice(0..6)
    val isAuth = call.request().url.encodedPath.contains("auth")

    if (isAuth) {
        Log.d(
            TAG,
            "resultFromCall: $uid is ${call.request().method} ${call.request().url} ** BODY CENSORED **"
        )
    } else {
        Log.d(
            TAG,
            "resultFromCall: $uid is ${call.request().method} ${call.request().url} ${call.request().body}"
        )
    }

    return if (cached != null) {
        Log.d(TAG, "resultFromCall: $uid cached; $cached")
        Result.success(cached)
    } else withContext(Dispatchers.IO) {
        Log.d(TAG, "resultFromCall: $uid not cached; calling ${call.request().url}")

        try {
            val response = call.execute()

            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(mapOnSuccess(body).also {
                    if (isAuth) {
                        Log.d(TAG, "resultFromCall: $uid success")
                    } else {
                        Log.d(TAG, "resultFromCall: $uid success: $it")
                    }
                })
            } else {
                val errorBody = response.errorBody()!!
                val errorString = errorBody.string()
                val json = JsonParser().parse(errorString)

                extraOnFailure?.invoke(errorBody)
                Result.failure(
                    ExceptionWithStatusCode(
                        json.asJsonObject.get("message").asString,
                        response.code()
                    ).also {
                        Log.d(TAG, "resultFromCall: $uid failed: $it")
                    }
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(
                e.also {
                    Log.d(TAG, "resultFromCall: $uid failed: $it")
                }
            )
        }
    }
}

class ExceptionWithStatusCode(
    override val message: String?,
    val statusCode: Int,
) : Exception(message)

private const val TAG = "NetworkUtil"