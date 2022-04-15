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
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val lazyPlaceSelectorViewModel: Lazy<PlaceSelectorViewModel> = viewModel()
    private val navController = NavHostController(this).apply {
        navigatorProvider.addNavigator(ComposeNavigator())
        navigatorProvider.addNavigator(DialogNavigator())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        setContent {
            ProvideWindowInsets(
                windowInsetsAnimationsEnabled = true,
                consumeWindowInsets = false,
            ) {
                DimigoinTheme {
                    App(navController, lazyPlaceSelectorViewModel)
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
    navController: NavHostController,
    lazyPlaceSelectorViewModel: Lazy<PlaceSelectorViewModel>,
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
                description = remark?.let { "사유: $it" },
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
        bottomBar = bottomBar@{
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = NoNavScreen.Splash.route,
            modifier = Modifier
                .padding(innerPadding),
        ) {
            preMainNavGraph(navController)
            mainNavGraph(navController, onPlaceChange, lazyPlaceSelectorViewModel)
            placeSelectorNavGraph(
                navController,
                onPlaceChange,
                onFavoriteAdd,
                onFavoriteRemove,
                lazyPlaceSelectorViewModel
            )
        }
        CustomSnackbarHost(snackbarHostState)
        CustomBottomBar(navController, lazyPlaceSelectorViewModel)

        val isInTransition = navController.visibleEntries.collectAsState().value.size >= 2
        if (isInTransition) {
            ClickPreventingBox()
        }
    }
}

@Composable
fun CustomBottomBar(
    navController: NavHostController,
    lazyPlaceSelectorViewModel: Lazy<PlaceSelectorViewModel>,
) {
    val placeSelectorViewModel by lazyPlaceSelectorViewModel

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val currentRoute = currentDestination?.route ?: return
    val currentScreen = Screen.findByRoute(currentRoute)

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        AnimatedVisibility(
            visible = currentScreen is NavScreen ||
                    (currentScreen is PlaceSelectorScreen && currentScreen.showCurrentPlace),
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it })
        ) {
            Crossfade(targetState = currentScreen) {
                if (it is NavScreen) {
                    BottomNavBar(navController, currentDestination)
                } else {
                    PlaceBottomBar(placeSelectorViewModel.currentPlace.collectAsState().value)
                }
            }
        }
    }
}

@Composable
fun PlaceBottomBar(currentPlace: Future<Place>) {
    if (!isSystemInDarkTheme()) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(30.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color(0f, 0f, 0f, 0.05f)
                        )
                    )
                )
        )
    }
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsHeight(80.dp)
            .padding(top = 20.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = buildAnnotatedString {
                    when (val v = currentPlace) {
                        is Future.Success -> {
                            append("나의 위치는 현재 ")
                            withStyle(SpanStyle(color = Point)) { append(v._data.name) }
                            append("입니다")
                        }
                        is Future.Failure -> append("위치 정보를 불러오지 못했습니다")
                        is Future.Loading, is Future.Nothing<*> -> append("위치 정보를 가져오는 중입니다")
                    }
                },
                style = DTypography.t4,
            )
            Spacer(Modifier.weight(1f))
            Spacer(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxWidth()
                    .navigationBarsHeight(),
            )
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController, currentDestination: NavDestination?) {
    val navScreens = listOf(
        NavScreen.Main,
        NavScreen.Meal,
        NavScreen.Calendar,
        NavScreen.Application,
        NavScreen.MyInfo,
    )
    Column {
        Box {
            BottomNavBarImpl(
                navController,
                navScreens,
                currentDestination
            )
        }
        Spacer(
            modifier = Modifier
                .background(Color.White)
                .fillMaxWidth()
                .navigationBarsHeight(),
        )
    }
}

fun NavGraphBuilder.preMainNavGraph(
    navController: NavHostController,
) {
    composable(NoNavScreen.Splash.route) {
        SplashScreen(
            onAutoLoginSuccess = {
                navController.navigate(NavScreen.Main.route) {
                    popUpTo(NoNavScreen.Splash.route) { inclusive = true }
                }
            },
            onAutoLoginFail = {
                navController.navigate(NoNavScreen.Login.route) {
                    popUpTo(NoNavScreen.Splash.route) { inclusive = true }
                }
            },
        )
    }
    composable(NoNavScreen.Login.route) {
        LoginScreen(
            modifier = Modifier,
            onLoginSuccess = {
                navController.navigate(NavScreen.Main.route) {
                    popUpTo(NoNavScreen.Login.route) { inclusive = true }
                }
            }
        )
    }
}

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    onPlaceChange: (Place, String?) -> Unit,
    lazyPlaceSelectorViewModel: Lazy<PlaceSelectorViewModel>
) {
    composable(NavScreen.Main.route) {
        val placeSelectorViewModel by lazyPlaceSelectorViewModel
        MainScreen(
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .padding(top = 36.dp)
                .systemBarsPadding(),
            onPlaceChange = {
                onPlaceChange(it, null)
            },
            onPlaceSelectorNavigate = {
                navController.navigate(PlaceSelectorScreen.Building.route)
                placeSelectorViewModel.selectedBuilding.value = "즐겨찾기"
            },
            onMealPageSelectorNavigate = {
                navController.navigate(NavScreen.Meal.route) {
                    navController.graph.findNode(NavScreen.Main.route)?.id?.let {
                        popUpTo(it) {
                            saveState = true
                        }
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onNotificationNavigate = {
                navController.navigate(NoNavScreen.Notification.route)
            },
            hasNewNotification = false
        )
    }
    composable(NavScreen.Meal.route) {
        MealScreen(
            onMealTimeClick = {
                navController.navigate(NoNavScreen.MealTime.type(it))
            },
        )
    }
    composable(
        NoNavScreen.MealTime.route,
        NoNavScreen.MealTime.navArguments
    ) {
        val startPage = it.arguments?.getInt("type") ?: 0
        MealTimeScreen(
            startPage = startPage,
            onBackPress = { navController.popBackStack() }
        )
    }
    composable(NavScreen.Calendar.route) { ScheduleScreen() }
    composable(NavScreen.Application.route) {
        ApplicationScreen(
            onApplicationClick = { route ->
                navController.navigate(route)
            }
        )
    }
    composable(NavScreen.MyInfo.route) { MyInfoScreen() }
    composable(NoNavScreen.Developing.route) {
        DevelopingScreen(
            backOnClick = {
                navController.popBackStack()
            }
        )
    }
    composable(NoNavScreen.Notification.route) {
        NotificationScreen(
            onBackNavigation = {
                navController.popBackStack()
            }
        )
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
    lazyPlaceSelectorViewModel: Lazy<PlaceSelectorViewModel>,
) {
    val navigateToRemark = { place: Place ->
        navController.navigate(PlaceSelectorScreen.SetRemark.place(place))
    }
    val navigateToRemarkFavorite = { place: Place ->
        navController.navigate(PlaceSelectorScreen.AddFavorite.place(place))
    }
    composable(PlaceSelectorScreen.Building.route) {
        val placeSelectorViewModel: PlaceSelectorViewModel by lazyPlaceSelectorViewModel

        placeSelectorViewModel.getCurrentPlace()

        BuildingScreen(
            modifier = Modifier.systemBarsPadding(),
            placeSelectorViewModel = placeSelectorViewModel,
            title = placeSelectorViewModel.selectedBuilding.value,
            onBackNavigation = { navController.popBackStack() },
            onSearch = { navController.navigate(PlaceSelectorScreen.Search.route) },
            onBuildingClick = { placeSelectorViewModel.selectedBuilding.value = it.name },
            onTryPlaceChange = navigateToRemark,
            onPlaceChange = onPlaceChange,
            onCategoryClick = {
                val category = "${placeSelectorViewModel.selectedBuilding.value} ${it.name}"
                navController.navigate(PlaceSelectorScreen.Category.category(category))
            },
            onTryFavoriteAdd = navigateToRemarkFavorite,
            onFavoriteRemove = onFavoriteRemove,
        )
    }
    composable(
        PlaceSelectorScreen.Category.route,
        PlaceSelectorScreen.Category.navArguments,
    ) {
        val category = it.arguments?.getString("category") ?: ""
        val placeSelectorViewModel: PlaceSelectorViewModel by lazyPlaceSelectorViewModel

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
    composable(PlaceSelectorScreen.Search.route) {
        val placeSelectorViewModel: PlaceSelectorViewModel by lazyPlaceSelectorViewModel

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
        PlaceSelectorScreen.SetRemark.route,
        PlaceSelectorScreen.SetRemark.navArguments,
        deepLinks = listOf(
            navDeepLink { uriPattern = "dimigoin://set-place/{placeId}" }
        )
    ) {
        val placeId = it.arguments?.getString("placeId") ?: ""
        val placeSelectorViewModel: PlaceSelectorViewModel by lazyPlaceSelectorViewModel
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
        PlaceSelectorScreen.AddFavorite.route,
        PlaceSelectorScreen.AddFavorite.navArguments,
    ) {
        val placeId = it.arguments?.getString("placeId") ?: ""
        val placeSelectorViewModel: PlaceSelectorViewModel by lazyPlaceSelectorViewModel

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
    screens: List<NavScreen>,
    currentDestination: NavDestination?,
) {
    BottomNavigation {
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(screen.icon),
                        contentDescription = null
                    )
                },
                label = { Text(screen.name, style = DTypography.t6) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        navController.graph.findNode(NavScreen.Main.route)?.id?.let {
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