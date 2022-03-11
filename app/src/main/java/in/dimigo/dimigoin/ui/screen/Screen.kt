package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import androidx.annotation.DrawableRes

sealed class Screen(val route: String, val name: String, @DrawableRes val icon: Int) {
    object Main : Screen("main", "메인", R.drawable.ic_main)
    object Meal : Screen("meal", "급식", R.drawable.ic_meal)
    object Calendar : Screen("calendar", "일정", R.drawable.ic_calendar)
    object Application : Screen("application", "신청", R.drawable.ic_application)
    object MyInfo : Screen("my_info", "학생증", R.drawable.ic_my_information)
}
