package `in`.dimigo.dimigoin.ui

import `in`.dimigo.dimigoin.ui.composables.BottomNavigation
import `in`.dimigo.dimigoin.ui.composables.BottomNavigationItem
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHost
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHostState
import `in`.dimigo.dimigoin.ui.screen.LoginScreen
import `in`.dimigo.dimigoin.ui.screen.Screen
import `in`.dimigo.dimigoin.ui.screen.SplashScreen
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import `in`.dimigo.dimigoin.viewmodel.LoginViewModel
import `in`.dimigo.dimigoin.viewmodel.SplashViewModel
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val splashViewModel by viewModel<SplashViewModel>()
    private val loginViewModel by viewModel<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screens = listOf(
            Screen.Main,
            Screen.Meal,
            Screen.Calendar,
            Screen.Application,
            Screen.MyInfo,
        )

        setContent {
            DimigoinTheme {
                App(screens, splashViewModel, loginViewModel)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(
    navBarScreens: List<Screen>,
    splashViewModel: SplashViewModel,
    loginViewModel: LoginViewModel,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { CustomSnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            val navBarVisible = navBarScreens.any { currentDestination?.route == it.route }
            AnimatedVisibility(visible = navBarVisible, enter = fadeIn(), exit = fadeOut()) {
                BottomNavBarImpl(navController, navBarScreens, currentDestination)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
                    splashViewModel = splashViewModel,
                    onAutoLoginSuccess = {
                        navController.navigate(Screen.Main.route) {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                    onAutoLoginFail = {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    },
                )
            }
            composable("login") {
                LoginScreen(
                    loginViewModel = loginViewModel,
                    onLoginSuccess = {
                        navController.navigate(Screen.Main.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Main.route) { Text(text = "메인") }
            composable(Screen.Meal.route) { Text(text = "급식") }
            composable(Screen.Calendar.route) { Text(text = "일정") }
            composable(Screen.Application.route) { Text(text = "신청") }
            composable(Screen.MyInfo.route) { Text(text = "내 정보") }
        }
        CustomSnackbarHost(snackbarHostState)
    }
}

@Composable
fun BottomNavBarImpl(navController: NavController, screens: List<Screen>, currentDestination: NavDestination?) {
    BottomNavigation {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(painter = painterResource(screen.icon),
                        contentDescription = null)
                },
                label = { Text(screen.name, style = DTypography.t6) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.findNode(Screen.Main.route)?.id?.let {
                            popUpTo(it) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
    }
}
