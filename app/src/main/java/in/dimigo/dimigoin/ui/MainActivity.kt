package `in`.dimigo.dimigoin.ui

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.util.josa
import `in`.dimigo.dimigoin.ui.composables.BottomNavigation
import `in`.dimigo.dimigoin.ui.composables.BottomNavigationItem
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHost
import `in`.dimigo.dimigoin.ui.composables.CustomSnackbarHostState
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.screen.*
import `in`.dimigo.dimigoin.ui.screen.meal.MealScreen
import `in`.dimigo.dimigoin.ui.screen.meal.MealTimeScreen
import `in`.dimigo.dimigoin.ui.screen.placeselector.BuildingScreen
import `in`.dimigo.dimigoin.ui.screen.placeselector.PlaceSearchScreen
import `in`.dimigo.dimigoin.ui.screen.placeselector.PlacesScreen
import `in`.dimigo.dimigoin.ui.screen.placeselector.ReasonScreen
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.DimigoinTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.*
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.*
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.systemBarsPadding
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

class MainActivity : AppCompatActivity() {

    private val navController = NavHostController(this).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val screens = listOf(
            Screen.Main,
            Screen.Meal,
            Screen.Calendar,
            Screen.Application,
            Screen.MyInfo,
        )

        setContent {
            ProvideWindowInsets(
                windowInsetsAnimationsEnabled = true,
                consumeWindowInsets = false,
            ) {
                DimigoinTheme {
                    App(screens, navController)
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}

@OptIn(ExperimentalAnimationApi::class, NavControllerVisibleEntries::class)
@Composable
fun App(
    navBarScreens: List<Screen>,
    navController: NavHostController,
) {
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
            AnimatedVisibility(
                visible = navBarVisible, enter = fadeIn(), exit = fadeOut()
            ) {
                Column {
                    BottomNavBarImpl(navController, navBarScreens, currentDestination)
                    Spacer(
                        modifier = Modifier
                            .background(Color.White)
                            .fillMaxWidth()
                            .navigationBarsHeight(),
                    )

                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier
                .padding(innerPadding),
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
                    modifier = Modifier,
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
                        .padding(top = 36.dp)
                        .systemBarsPadding(),
                    onPlaceSelectorNavigate = { navController.navigate("building") },
                    hasNewNotification = false
                )
            }
            composable(Screen.Meal.route) {
                MealScreen(
                    onMealTimeClick = { navController.navigate("meal_time/$it") },
                )
            }
            composable(
                "meal_time/{type}",
                arguments = listOf(navArgument("type") { type = NavType.IntType })
            ) {
                val startPage = it.arguments?.getInt("type") ?: 0
                MealTimeScreen(
                    startPage = startPage,
                    onBackPress = { navController.popBackStack() }
                )
            }
            composable(Screen.Calendar.route) { ScheduleScreen() }
            composable(Screen.Application.route) {
                ApplicationScreen(
                    onApplicationClick = { route ->
                        navController.navigate(route)
                    }
                )
            }
            composable(Screen.MyInfo.route) { Text(text = "내 정보") }
            composable(Screen.MyInfo.route) { MyInfoScreen() }
            composable("developing") {
                DevelopingScreen(
                    Modifier.systemBarsPadding(),
                    backOnClick = {
                        navController.popBackStack()
                    }
                )
            }
            placeSelectorNavGraph(navController, onPlaceChange, onFavoriteAdd, onFavoriteRemove)
        }
        CustomSnackbarHost(snackbarHostState)

        val isInTransition = navController.visibleEntries.collectAsState().value.size >= 2
        if (isInTransition) {
            ClickPreventingBox()
        }
    }
}

@Composable
fun ClickPreventingBox() {
    Box(
        Modifier
            .fillMaxSize()
            .noRippleClickable { }
    )
}

fun NavGraphBuilder.placeSelectorNavGraph(
    navController: NavController,
    onPlaceChange: (Place, String?) -> Unit,
    onFavoriteAdd: (Place, String?) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) {
    val navigateToRemark = { place: Place ->
        navController.navigate("remark/${place._id}")
    }
    val navigateToRemarkFavorite = { place: Place ->
        navController.navigate("remark_favorite/${place._id}")
    }
    composable("building") {
        val placeSelectorViewModel: PlaceSelectorViewModel = getViewModel()

        BuildingScreen(
            modifier = Modifier.systemBarsPadding(),
            placeSelectorViewModel = placeSelectorViewModel,
            title = placeSelectorViewModel.selectedBuilding.value,
            onBackNavigation = { navController.popBackStack() },
            onSearch = { navController.navigate("search") },
            onBuildingClick = { placeSelectorViewModel.selectedBuilding.value = it.name },
            onTryPlaceChange = navigateToRemark,
            onPlaceChange = onPlaceChange,
            onCategoryClick = { navController.navigate("category/${placeSelectorViewModel.selectedBuilding.value} ${it.name}") },
            onTryFavoriteAdd = navigateToRemarkFavorite,
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
        val placeSelectorViewModel: PlaceSelectorViewModel = getViewModel()

        PlacesScreen(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
                .systemBarsPadding(),
            placeSelectorViewModel = placeSelectorViewModel,
            title = category,
            onBackNavigation = { navController.popBackStack() },
            onTryPlaceChange = navigateToRemark,
            onTryFavoriteAdd = navigateToRemarkFavorite,
            onFavoriteRemove = onFavoriteRemove,
        )
    }
    composable("search") {
        val placeSelectorViewModel: PlaceSelectorViewModel = getViewModel()

        PlaceSearchScreen(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
                .systemBarsPadding(),
            placeSelectorViewModel = placeSelectorViewModel,
            onBackNavigation = { navController.popBackStack() },
            onTryPlaceChange = navigateToRemark,
            onTryFavoriteAdd = navigateToRemarkFavorite,
            onFavoriteRemove = onFavoriteRemove,
            color = Color.Black
        )
    }
    composable(
        "remark/{placeId}",
        arguments = listOf(
            navArgument("placeId") { type = NavType.StringType },
        ),
        deepLinks = listOf(
            navDeepLink { uriPattern = "dimigoin://set-place/{placeId}" }
        )
    ) {
        val placeId = it.arguments?.getString("placeId") ?: ""
        val placeSelectorViewModel: PlaceSelectorViewModel = getViewModel()
        Log.d(TAG, "placeSelectorNavGraph: $placeId")
        Log.d(TAG, "placeSelectorNavGraph: ${placeSelectorViewModel.placeIdToPlace(placeId)}")

        val isPlaceLoaded = placeSelectorViewModel.isPlaceLoaded.collectAsState().value
        if (isPlaceLoaded) {
            ReasonScreen(
                modifier = Modifier
                    .background(MaterialTheme.colors.surface)
                    .fillMaxWidth()
                    .systemBarsPadding()
                    .navigationBarsWithImePadding(),
                place = placeSelectorViewModel.placeIdToPlace(placeId),
                onConfirm = { place, remark ->
                    placeSelectorViewModel.setCurrentPlace(place, remark, onPlaceChange)
                    navController.popBackStack()
                    Unit
                },
                isFavoriteRegister = false,
                onBackNavigation = { navController.popBackStack() },
            )
        }
    }
    composable(
        "remark_favorite/{placeId}",
        arguments = listOf(
            navArgument("placeId") { type = NavType.StringType },
        ),
    ) {
        val placeId = it.arguments?.getString("placeId") ?: ""
        val placeSelectorViewModel: PlaceSelectorViewModel = getViewModel()

        ReasonScreen(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxWidth()
                .systemBarsPadding()
                .navigationBarsWithImePadding(),
            place = placeSelectorViewModel.placeIdToPlace(placeId),
            onConfirm = { place, remark ->
                placeSelectorViewModel.addFavoriteAttendanceLog(place, remark, onFavoriteAdd)
                navController.popBackStack()
                Unit
            },
            isFavoriteRegister = true,
            onBackNavigation = { navController.popBackStack() },
        )
    }
}

@Composable
fun BottomNavBarImpl(
    navController: NavController,
    screens: List<Screen>,
    currentDestination: NavDestination?,
) {
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

private const val TAG = "MainActivity"