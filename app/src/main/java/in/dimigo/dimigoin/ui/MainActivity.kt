package `in`.dimigo.dimigoin.ui

import `in`.dimigo.dimigoin.ui.composables.BottomNavigation
import `in`.dimigo.dimigoin.ui.composables.BottomNavigationItem
import `in`.dimigo.dimigoin.ui.screen.Screen
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
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
                App(screens)
            }
        }
    }
}

@Composable
fun App(
    screens: List<Screen>,
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigation {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

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
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Main.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Main.route) { Text(text = "메인") }
            composable(Screen.Meal.route) { Text(text = "급식") }
            composable(Screen.Calendar.route) { Text(text = "일정") }
            composable(Screen.Application.route) { Text(text = "신청") }
            composable(Screen.MyInfo.route) { Text(text = "내 정보") }
        }
    }
}
