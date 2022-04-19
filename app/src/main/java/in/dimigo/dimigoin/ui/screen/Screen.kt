package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.ui.composables.MealTimeType
import androidx.annotation.DrawableRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed interface Screen {
    val route: String
    val navArguments: List<NamedNavArgument>
    val bottomBarType: BottomBarType

    fun matchesRoute(route: String): Boolean = this.route == route

    companion object {
        private val values = listOf(
            NavScreen.Main,
            NavScreen.Meal,
            NavScreen.Calendar,
            NavScreen.Application,
            NavScreen.MyInfo,
            NoNavScreen.Splash,
            NoNavScreen.Login,
            NoNavScreen.MealTime,
            NoNavScreen.Developing,
            NoNavScreen.Notification,
            PlaceSelectorScreen.AddFavorite,
            PlaceSelectorScreen.Building,
            PlaceSelectorScreen.Category,
            PlaceSelectorScreen.Search,
            PlaceSelectorScreen.SetRemark,
        )

        fun findByRoute(route: String): Screen? {
            return values.find { it.matchesRoute(route) }
        }
    }
}

sealed class NavScreen(
    override val route: String,
    val name: String,
    @DrawableRes val icon: Int,
    override val bottomBarType: BottomBarType = BottomBarType.MAIN,
) : Screen {
    override val navArguments: List<NamedNavArgument> = emptyList()

    object Main : NavScreen("main", "메인", R.drawable.ic_main)
    object Meal : NavScreen("meal", "급식", R.drawable.ic_meal)
    object Calendar : NavScreen("calendar", "일정", R.drawable.ic_calendar)
    object Application : NavScreen("application", "신청", R.drawable.ic_application)
    object MyInfo : NavScreen("my_info", "학생증", R.drawable.ic_my_information)
}

sealed class NoNavScreen(
    override val route: String,
    override val navArguments: List<NamedNavArgument>,
    override val bottomBarType: BottomBarType = BottomBarType.NONE,
) : Screen {
    object Splash : NoNavScreen("splash", emptyList())
    object Login : NoNavScreen("login", emptyList())
    object MealTime : NoNavScreen(
        "meal_time/{type}",
        listOf(
            navArgument("type") { type = NavType.IntType }
        )
    ) {
        override fun matchesRoute(route: String): Boolean {
            val regex = """meal_time/\d+""".toRegex()
            return route.matches(regex)
        }
        fun type(type: MealTimeType) = route.replace("{type}", type.integerValue.toString())
    }
    object Developing : NoNavScreen("developing", emptyList())
    object Notification : NoNavScreen("notification", emptyList())
}

sealed class PlaceSelectorScreen(
    override val route: String,
    override val navArguments: List<NamedNavArgument>,
    val showCurrentPlace: Boolean,
    override val bottomBarType: BottomBarType = BottomBarType.PLACE_SELECTOR,
) : Screen {
    object Building : PlaceSelectorScreen("ps_building", emptyList(), true)
    object Category : PlaceSelectorScreen(
        "ps_category/{category}",
        listOf(
            navArgument("category") { type = NavType.StringType }
        ),
        false
    ) {
        override fun matchesRoute(route: String): Boolean {
            val regex = """ps_category/.+""".toRegex()
            return route.matches(regex)
        }
        fun category(category: String) = route.replace("{category}", category)
    }
    object Search : PlaceSelectorScreen("ps_search", emptyList(), false)
    object SetRemark : PlaceSelectorScreen(
        "ps_set_remark/{placeId}",
        listOf(
            navArgument("placeId") { type = NavType.StringType }
        ),
        false
    ) {
        override fun matchesRoute(route: String): Boolean {
            val regex = """ps_set_remark/.+""".toRegex()
            return route.matches(regex)
        }
        fun place(place: Place) = route.replace("{placeId}", place._id)
        fun place(placeId: String) = route.replace("{placeId}", placeId)
    }
    object AddFavorite : PlaceSelectorScreen(
        "ps_add_favorite/{placeId}",
        listOf(
            navArgument("placeId") { type = NavType.StringType }
        ),
        false
    ) {
        override fun matchesRoute(route: String): Boolean {
            val regex = """ps_add_favorite/.+""".toRegex()
            return route.matches(regex)
        }
        fun place(place: Place) = route.replace("{placeId}", place._id)
        fun place(placeId: String) = route.replace("{placeId}", placeId)
    }
}

enum class BottomBarType {
    PLACE_SELECTOR,
    MAIN,
    NONE,
}