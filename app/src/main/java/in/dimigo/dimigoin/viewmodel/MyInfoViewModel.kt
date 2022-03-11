package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.launch

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
            .setTitle("본인 인증")
            .setSubtitle("학생증을 사용하기 위해 인증이 필요합니다.")
            .setAllowedAuthenticators(BIOMETRIC_WEAK or DEVICE_CREDENTIAL)
            .build()
        biometricPrompt.authenticate(promptInfo)
    }

    fun renderBarcode(width: Int, height: Int): Bitmap? {
        val barcodeString = myIdentity?.libraryId ?: return null
        val bitMatrix = MultiFormatWriter().encode(barcodeString, BarcodeFormat.CODE_39, width, height)
        val bitmap = Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                val color = if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT
                bitmap.setPixel(x, y, color)
            }
        }
        return bitmap
    }
}