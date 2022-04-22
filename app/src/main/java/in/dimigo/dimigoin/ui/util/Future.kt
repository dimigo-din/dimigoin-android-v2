package `in`.dimigo.dimigoin.ui.util

import java.lang.IllegalStateException
import java.util.*

sealed class Future<T : Any> {
    class Loading<T : Any> : Future<T>()
    class Success<T : Any>(val data: T) : Future<T>()
    class Failure<T : Any>(val throwable: Throwable) : Future<T>()

    fun asOptional() = when (val future = this) {
        is Success -> Optional.of(future.data)
        else -> Optional.empty()
    }

    companion object {
        fun <T : Any> loading() = Loading<T>()
        fun <T : Any> success(data: T) = Success(data)
        fun <T : Any> failure(throwable: Throwable) = Failure<T>(throwable)
    }
}

fun <T : Any> Result<T>.asFuture(result: Result<T>) = if (isSuccess) {
    Future.success(result.getOrThrow())
} else {
    Future.failure(result.exceptionOrNull() ?: throw IllegalStateException())
}
