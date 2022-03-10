package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.BorderTextField
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Red
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel = getViewModel(),
    onLoginSuccess: () -> Unit,
) = Box(modifier.fillMaxSize()) {
    var username by remember { mutableStateOf(TextFieldValue()) }
    var password by remember { mutableStateOf(TextFieldValue()) }

    Column(
        Modifier.align(Alignment.Center)
    ) {
        Image(
            modifier = modifier.padding(bottom = 50.dp),
            painter = painterResource(id = R.drawable.ic_dimigoin),
            contentDescription = null
        )
        Spacer(modifier = modifier.height(48.dp))
        when (val v = loginViewModel.loginResult.collectAsState().value) {
            is Future.Nothing<*> -> {
                BorderTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "아이디를 입력하세요"
                )
                Spacer(modifier = modifier.height(10.dp))
                BorderTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "비밀번호를 입력하세요",
                    visualTransformation = PasswordVisualTransformation()
                )
                Spacer(modifier = modifier.height(50.dp))
            }
            is Future.Success<*> -> onLoginSuccess()
//            is Future.Failure<*> -> Text(text = "Login failed. ${v.throwable.message}")
            is Future.Failure<*> -> {
                BorderTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = "아이디를 입력하세요",
                    color = Red
                )
                Spacer(modifier = modifier.height(10.dp))
                BorderTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = "비밀번호를 입력하세요",
                    visualTransformation = PasswordVisualTransformation(),
                    color = Red
                )
                Spacer(modifier = modifier.height(18.dp))
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
                Spacer(modifier = modifier.height(17.dp))
            }
            is Future.Loading<*> -> CircularProgressIndicator()
        }
        Button(modifier = modifier
            .fillMaxWidth()
            .background(color = Point, shape = RoundedCornerShape(30))
            .padding(vertical = 15.dp),
            onClick = {
                loginViewModel.login(username.text, password.text)
            }
        ) {
            Text(
                text = "로그인",
                style = DTypography.t3,
                textAlign = TextAlign.Center,
                color = Color.White
            )
        }

    }

}

@Preview(showSystemUi = true)
@Composable
fun LoginPrev() {
    LoginScreen() {

    }
}
