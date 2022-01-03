package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val userLoginUseCase: UserLoginUseCase,
) : ViewModel() {

    private val _loginResult = MutableStateFlow<Future<Boolean>>(Future.Nothing())
    val loginResult = _loginResult.asStateFlow()

    fun login(username: String, password: String) = viewModelScope.launch {
        _loginResult.emit(Future.Loading())
        userLoginUseCase(username, password).onSuccess {
            _loginResult.emit(Future.Success(true))
        }.onFailure {
            _loginResult.emit(Future.Failure(it))
            Log.d(TAG, "login: $it")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
