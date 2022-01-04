package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
) : ViewModel() {

    private val _autoLogin = MutableStateFlow<Future<Boolean>>(Future.Nothing())
    val autoLogin = _autoLogin.asStateFlow()

    init {
        getMyIdentity()
    }

    private fun getMyIdentity() = viewModelScope.launch {
        _autoLogin.emit(Future.Loading())
        getMyIdentityUseCase().onSuccess {
            _autoLogin.emit(Future.Success(true))
            Log.d(TAG, "getMyIdentity: $it")
        }.onFailure {
            _autoLogin.emit(Future.Success(false))
            Log.d(TAG, "getMyIdentity: $it")
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
