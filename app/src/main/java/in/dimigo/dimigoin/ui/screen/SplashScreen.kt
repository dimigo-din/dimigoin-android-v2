package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.SplashViewModel
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import org.koin.androidx.compose.getViewModel

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel = getViewModel(),
    onAutoLoginSuccess: () -> Unit,
    onAutoLoginFail: () -> Unit,
) {
    val autoLogin = splashViewModel.autoLogin.collectAsState().value

    LaunchedEffect(autoLogin) {
        when (autoLogin) {
            is Future.Success -> onAutoLoginSuccess()
            is Future.Failure -> onAutoLoginFail()
            else -> {}
        }
    }
}

private const val TAG = "SplashScreen"
