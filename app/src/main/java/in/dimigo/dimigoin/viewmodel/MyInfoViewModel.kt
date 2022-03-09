package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.MainActivity
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class MyInfoViewModel(
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    var myIdentity: User? = null

    init {
        getMyIdentity()
    }

    private fun getMyIdentity() = viewModelScope.launch {
        getMyIdentityUseCase().onSuccess { myIdentity = it }
    }

    fun runWhenAuthenticated(onSucceeded: () -> Unit, context: Context) {
        val executor = ContextCompat.getMainExecutor(context)
        val callback = @RequiresApi(Build.VERSION_CODES.P)
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                onSucceeded()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                if (errorCode != BiometricPrompt.ERROR_USER_CANCELED)
                    Toast.makeText(context, "인증을 사용할 수 없습니다. 기기 잠금이 활성화 되어있는지 확인해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
        val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, callback)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("인증")
            .setSubtitle("학생증을 사용하기 위해 인증이 필요합니다.")
            .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }
}