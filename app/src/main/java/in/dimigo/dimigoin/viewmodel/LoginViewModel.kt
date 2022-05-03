package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.data.util.ExceptionWithStatusCode
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

    private val _uiState = MutableStateFlow(UiState(LoginState.NotPerformed))
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String) = viewModelScope.launch {
        _uiState.emit(uiState.value.copy(loginState = LoginState.InProgress))
        userLoginUseCase(username, password).onSuccess {
            if (it) {
                _uiState.emit(uiState.value.copy(loginState = LoginState.Success))
            } else {
                _uiState.emit(uiState.value.copy(loginState = LoginState.Success))
            }
        }.onFailure {
            if (it is ExceptionWithStatusCode) {
                _uiState.emit(uiState.value.copy(loginState = LoginState.CredentialError(it)))
            } else {
                _uiState.emit(uiState.value.copy(loginState = LoginState.NetworkError(it)))
            }
            Log.d(TAG, "login: $it")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }

    data class UiState(
        val loginState: LoginState
    )
}

sealed class LoginState {
    object NotPerformed : LoginState()
    object InProgress : LoginState()
    data class NetworkError(val throwable: Throwable) : LoginState()
    data class CredentialError(val throwable: Throwable) : LoginState()
    object Success : LoginState()
}
