package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.ui.util.Future
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
        try {
            val response = userLoginUseCase(username, password)
            _loginResult.emit(Future.Success(response))
        } catch (e: Exception) {
            _loginResult.emit(Future.Failure(e))
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
