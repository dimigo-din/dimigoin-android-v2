package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginSuccess: () -> Unit,
) = Column {

    when (val v = loginViewModel.loginResult.collectAsState().value) {
        is Future.Success<*> -> onLoginSuccess()
        is Future.Failure<*> -> Text(text = "Login failed. ${v.throwable.message}")
        is Future.Loading<*> -> CircularProgressIndicator()
        is Future.Nothing<*> -> {}
    }

    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BasicTextField(value = username, onValueChange = { username = it })
    BasicTextField(
        value = password,
        onValueChange = { password = it },
        visualTransformation = PasswordVisualTransformation()
    )
    Button(onClick = { loginViewModel.login(username, password) }) {
        Text(text = "Login")
    }
}
