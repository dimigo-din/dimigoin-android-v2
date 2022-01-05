package `in`.dimigo.dimigoin.ui

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.util.josa
import `in`.dimigo.dimigoin.ui.composables.BottomNavigation
import `in`.dimigo.dimigoin.ui.composables.BottomNavigationItem
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHost
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHostState
import `in`.dimigo.dimigoin.ui.screen.LoginScreen
import `in`.dimigo.dimigoin.ui.screen.MainScreen
import `in`.dimigo.dimigoin.ui.screen.Screen
import `in`.dimigo.dimigoin.ui.screen.SplashScreen
import `in`.dimigo.dimigoin.ui.screen.placeselector.BuildingScreen
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App(
    navBarScreens: List<Screen>,
) {
    val navController = rememberNavController()
    val snackbarHostState = remember { CustomSnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val changePainter = painterResource(id = R.drawable.ic_location_change)
    val starPainter = painterResource(id = R.drawable.ic_favorite)
    val onPlaceChange = { place: Place, remark: String? ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                painter = changePainter,
                message = buildAnnotatedString {
                    append("내 위치를 ")
                    withStyle(SpanStyle(color = Point)) { append(place.name) }
                    append("${place.name.josa("으로", true)} 변경 완료")
                },
                description = "사유: $remark",
            )
        }
        Unit
    }
    val onFavoriteAdd = { place: Place, remark: String? ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                painter = starPainter,
                message = buildAnnotatedString {
                    withStyle(SpanStyle(color = Point)) { append(place.name) }
                    append("${place.name.josa("이가", true)} 즐겨찾기에 추가됨")
                },
                description = "사유: $remark",
            )
        }
        Unit
    }
    val onFavoriteRemove = { place: Place ->
        coroutineScope.launch {
            snackbarHostState.showSnackbar(
                painter = starPainter,
                message = buildAnnotatedString {
                    withStyle(SpanStyle(color = Point)) { append(place.name) }
                    append("${place.name.josa("이가", true)} 즐겨찾기에서 제거됨")
                },
            )
        }
        Unit
    }

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
            modifier = Modifier
                .padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
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
                    onLoginSuccess = {
                        navController.navigate(Screen.Main.route) {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                )
            }
            composable(Screen.Main.route) {
                MainScreen(
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .padding(top = 36.dp),
                    onPlaceSelectorNavigate = { navController.navigate("place_selector") },
                    onPlaceSelect = { },
                    hasNewNotification = false
                )
            }
            composable(Screen.Meal.route) { Text(text = "급식") }
            composable(Screen.Calendar.route) { Text(text = "일정") }
            composable(Screen.Application.route) { Text(text = "신청") }
            composable(Screen.MyInfo.route) { Text(text = "내 정보") }
            placeSelectorNavGraph(navController, onPlaceChange, onFavoriteAdd, onFavoriteRemove)
        }
        CustomSnackbarHost(snackbarHostState)
    }
}

fun NavGraphBuilder.placeSelectorNavGraph(
    navController: NavController,
    onPlaceChange: (Place, String?) -> Unit,
    onFavoriteAdd: (Place, String?) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) {
    navigation("building", "place_selector") {
        composable(
            "building",
            arguments = listOf(navArgument("building") { type = NavType.StringType })
        ) {
            val viewModel: PlaceSelectorViewModel = getViewModel()
            BuildingScreen(
                title = viewModel.selectedBuilding.value,
                onBackNavigation = { navController.popBackStack() },
                onSearch = { navController.navigate("search") },
                onBuildingClick = { viewModel.selectedBuilding.value = it.name },
                onPlaceChange = onPlaceChange,
                onCategoryClick = { navController.navigate("category/${viewModel.selectedBuilding.value} ${it.name}")},
                onFavoriteAdd = onFavoriteAdd,
                onFavoriteRemove = onFavoriteRemove,
            )
        }
        composable(
            "category/{category}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
            ),
        ) {
            val category = it.arguments?.getString("category") ?: ""
            Text(text = "Category $category")
        }
        composable("search") {
            Text(text = "Search")
        }
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
