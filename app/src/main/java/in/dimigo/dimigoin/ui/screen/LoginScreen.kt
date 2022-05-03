package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.theme.BorderTextField
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Red
import `in`.dimigo.dimigoin.viewmodel.LoginState
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    uiState: LoginViewModel.UiState,
    onLogin: (username: String, password: String) -> Unit,
    onLoginSuccess: () -> Unit,
) = Box(modifier.fillMaxSize()) {
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    val coroutineScope = rememberCoroutineScope()
    val bringIntoViewRequester = BringIntoViewRequester()

    Column(
        Modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .navigationBarsWithImePadding()
            .padding(horizontal = 28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .size(45.dp),
            painter = painterResource(id = R.drawable.ic_dimigoin),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(50.dp))
        BorderTextField(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent {
                    if (it.isFocused || it.hasFocus) {
                        coroutineScope.launch {
                            delay(250)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            value = username,
            onValueChange = { username = it },
            label = "아이디를 입력하세요",
            keyboardActions = KeyboardActions(onNext = {
                focusManager.moveFocus(FocusDirection.Down)
            }),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            borderColor = if (uiState.loginState is LoginState.CredentialError) {
                DTheme.colors.error
            } else {
                DTheme.colors.c2
            },
            textColor = DTheme.colors.onSurface
        )
        Spacer(modifier = Modifier.height(10.dp))
        BorderTextField(
            modifier = Modifier
                .bringIntoViewRequester(bringIntoViewRequester)
                .onFocusEvent {
                    if (it.isFocused || it.hasFocus) {
                        coroutineScope.launch {
                            delay(250)
                            bringIntoViewRequester.bringIntoView()
                        }
                    }
                },
            value = password,
            onValueChange = { password = it },
            label = "비밀번호를 입력하세요",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus()
            }),
            visualTransformation = PasswordVisualTransformation(),
            borderColor = if (uiState.loginState is LoginState.CredentialError) {
                DTheme.colors.error
            } else {
                DTheme.colors.c2
            },
            textColor = DTheme.colors.onSurface
        )

        Box(
            Modifier.height(60.dp),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState.loginState) {
                is LoginState.Success -> {
                    LaunchedEffect(state) {
                        onLoginSuccess()
                    }
                }
                is LoginState.CredentialError -> {
                    Text(
                        text = "존재하지 않는 아이디거나 잘못된 패스워드입니다.",
                        style = DTheme.typography.t6,
                        color = Red
                    )
                }
                is LoginState.NetworkError -> {
                    Text(
                        text = "네트워크 연결을 확인해주세요.",
                        style = DTheme.typography.t6,
                        color = Red
                    )
                }
                else -> {}
            }
        }

        val hasEnoughCredentials = username.text.isNotBlank() && password.text.isNotBlank()
        val isLoginInProgress = uiState.loginState is LoginState.InProgress
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = hasEnoughCredentials && !isLoginInProgress,
            onClick = {
                if (hasEnoughCredentials) {
                    onLogin(username.text, password.text)
                }
            },
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(contentColor = Point)
        ) {
            if (uiState.loginState is LoginState.InProgress) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    text = "로그인",
                    style = DTheme.typography.t5,
                    color = Color.White,
                )
            }
        }

    }
}
