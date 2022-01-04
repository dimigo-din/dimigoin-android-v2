package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.viewmodel.SplashViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onAutoLoginSuccess: () -> Unit,
    onAutoLoginFail: () -> Unit,
) {
    val autoLogin = splashViewModel.autoLogin.collectAsState().value

    LaunchedEffect(autoLogin) {
        when (autoLogin.data) {
            true -> onAutoLoginSuccess()
            false -> onAutoLoginFail()
        }
    }
}

private const val TAG = "SplashScreen"
