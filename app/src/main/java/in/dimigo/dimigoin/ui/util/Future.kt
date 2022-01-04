package `in`.dimigo.dimigoin.ui.util

sealed class Future<T>(val data: T?, val message: String?) {
    class Nothing<T> : Future<T>(null, null)
    class Loading<T> : Future<T>(null, null)
    class Success<T>(val _data: T) : Future<T>(_data, null)
    class Failure<T>(val throwable: Throwable) : Future<T>(null, throwable.message)
}
