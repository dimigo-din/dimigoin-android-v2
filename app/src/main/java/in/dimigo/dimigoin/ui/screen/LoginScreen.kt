package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.BorderTextField
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Red
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = getViewModel(),
    onLoginSuccess: () -> Unit,
) = Box(modifier.fillMaxSize()) {
    val focusManager = LocalFocusManager.current
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }
    var color by remember { mutableStateOf(C2) }
    var isLoading by remember { mutableStateOf(false) }

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
            color = color
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
            color = color
        )
        when (val v = loginViewModel.loginResult.collectAsState().value) {
            is Future.Nothing<*> -> {
                Spacer(modifier = Modifier.height(50.dp))
            }
            is Future.Success<*> -> onLoginSuccess()
            is Future.Failure<*> -> {
                color = Red
                isLoading = false
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    modifier = modifier
                        .noRippleClickable {
                            Toast.makeText(
                                LocalContext.current,
                                "Login failed. ${v.throwable.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                    text = "존재하지 않는 아이디거나 잘못된 패스워드입니다.",
                    style = DTypography.t6,
                    color = Red
                )
                Spacer(modifier = Modifier.height(17.dp))
            }
            is Future.Loading<*> -> {
                isLoading = true
                Spacer(modifier = Modifier.height(50.dp))
            }
        }
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { loginViewModel.login(username.text, password.text) },
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(contentColor = Point)
        ) {
            if (!isLoading) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    textAlign = TextAlign.Center,
                    text = "로그인",
                    style = DTypography.t5,
                    color = Color.White,
                )
            } else {
                CircularProgressIndicator(modifier = Modifier, color = Color.White)
            }
        }

    }
}
