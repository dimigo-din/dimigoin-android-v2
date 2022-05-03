package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.data.repository.fake.FakeUserRepository
import `in`.dimigo.dimigoin.domain.usecase.user.UserLoginUseCase
import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()
    lateinit var viewModel: LoginViewModel
    var loginSuccess: Boolean = false

    @Before
    fun setupViewModel() {
        viewModel = LoginViewModel(UserLoginUseCase(FakeUserRepository()))

        composeTestRule.setContent {
            DimigoinTheme {
                LoginScreen(
                    uiState = viewModel.uiState.collectAsState().value,
                    onLogin = { username, password ->  viewModel.login(username, password) },
                    onLoginSuccess = { loginSuccess = true },
                )
            }
        }

        loginSuccess = false
    }

    @Test
    fun button_withoutAnyCredentials_shouldBeDisabled() {
        composeTestRule.onNodeWithText("로그인").assertIsNotEnabled()
    }

    @Test
    fun button_withoutPassword_shouldBeDisabled() {
        composeTestRule.onNodeWithText("아이디를 입력하세요").performTextInput("aa")
        composeTestRule.onNodeWithText("로그인").assertIsNotEnabled()
    }

    @Test
    fun button_withoutUsername_shouldBeDisabled() {
        composeTestRule.onNodeWithText("비밀번호를 입력하세요").performTextInput("aa")
        composeTestRule.onNodeWithText("로그인").assertIsNotEnabled()
    }

    @Test
    fun button_withEnoughCredentials_shouldBeEnabled() {
        composeTestRule.onNodeWithText("아이디를 입력하세요").performTextInput("aa")
        composeTestRule.onNodeWithText("비밀번호를 입력하세요").performTextInput("aa")
        composeTestRule.onNodeWithText("로그인").assertIsEnabled()
    }

    @Test
    fun wrongCredential_shouldDisplayCredentialError() {
        composeTestRule.onNodeWithText("아이디를 입력하세요").performTextInput("wrong_credential")
        composeTestRule.onNodeWithText("비밀번호를 입력하세요").performTextInput("wrong_password**")
        composeTestRule.onNodeWithText("로그인").performClick()

        composeTestRule.mainClock.advanceTimeBy(3000)

        composeTestRule.onNodeWithText("존재하지 않는 아이디거나 잘못된 패스워드입니다.").assertExists()
    }

    @Test
    fun rightCredential_shouldDisplayNothing() {
        composeTestRule.onNodeWithText("아이디를 입력하세요").performTextInput("test_account")
        composeTestRule.onNodeWithText("비밀번호를 입력하세요").performTextInput("password!!")
        composeTestRule.onNodeWithText("로그인").performClick()

        composeTestRule.mainClock.advanceTimeBy(3000)

        assert(loginSuccess)
    }
}