package `in`.dimigo.dimigoin.data.util

import android.util.Log
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun <T, R> resultFromCall(
    call: Call<T>,
    cached: R? = null,
    extraOnFailure: ((errorBody: ResponseBody) -> Unit)? = null,
    mapOnSuccess: ((T) -> R),
): Result<R> {
    return if (cached != null) {
        Log.d(TAG, "resultFromCall: returning cached $cached in call ${call.request().url}")
        Result.success(cached)
    } else withContext(Dispatchers.IO) {
        Log.d(TAG, "resultFromCall: not cached, calling ${call.request().url}")

        try {
            val response = call.execute()

            if (response.isSuccessful) {
                val body = response.body()!!
                Result.success(mapOnSuccess(body))
            } else {
                val errorBody = response.errorBody()!!
                val errorString = errorBody.string()
                val json = JsonParser().parse(errorString)

                extraOnFailure?.invoke(errorBody)
                Result.failure(ExceptionWithStatusCode(json.asJsonObject.get("message").asString, response.code()))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }
}

class ExceptionWithStatusCode(
    override val message: String?,
    val statusCode: Int,
) : Exception(message)

private const val TAG = "NetworkUtil"