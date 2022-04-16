package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.ui.composables.ContentBox
import `in`.dimigo.dimigoin.ui.composables.MealTimeType
import `in`.dimigo.dimigoin.ui.composables.OnLifecycleEvent
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.ui.util.asKorean12HoursString
import `in`.dimigo.dimigoin.ui.util.icon
import `in`.dimigo.dimigoin.viewmodel.MainViewModel
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.google.accompanist.pager.ExperimentalPagerApi
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = getViewModel(),
    onPlaceChange: (Place) -> Unit,
    onPlaceSelectorNavigate: () -> Unit,
    onMealPageSelectorNavigate: () -> Unit,
    onNotificationNavigate: () -> Unit,
    hasNewNotification: Boolean,
) = Column(modifier) {
    val currentPlace = mainViewModel.currentPlace.collectAsState().value
    val mealTime = mainViewModel.mealTime.collectAsState().value
    val todayMeal = mainViewModel.todayMeal.collectAsState().value
    val pagerState = remember { mutableStateOf(mainViewModel.getCurrentMealType()) }
    val currentMealTypeByPage = when (pagerState.value) {
        0 -> MealTimeType.BREAKFAST.value
        1 -> MealTimeType.LUNCH.value
        2 -> MealTimeType.DINNER.value
        else -> ""
    }

    OnLifecycleEvent { _, event ->
        if (event == Lifecycle.Event.ON_RESUME) {
            mainViewModel.getCurrentPlace()
        }
    }

    Row(Modifier.fillMaxWidth()) {
        val notificationIcon = if (hasNewNotification) {
            R.drawable.ic_notification_true
        } else {
            R.drawable.ic_notification_false
        }
        Spacer(Modifier.width(10.dp))
        Icon(painter = painterResource(id = R.drawable.ic_dimigoin),
            contentDescription = null,
            tint = Point)
        Spacer(Modifier.weight(1f))
        Icon(
            painter = painterResource(id = notificationIcon), contentDescription = null,
            modifier = Modifier.noRippleClickable { onNotificationNavigate() }
        )
    }

    Spacer(Modifier.height(26.dp))

    ContentBox(
        title = "나의 위치",
        summary = buildAnnotatedString {
            when (currentPlace) {
                is Future.Success -> {
                    append("나의 위치는 현재 ")
                    withStyle(SpanStyle(color = Point)) { append(currentPlace._data.name) }
                    append("입니다")
                }
                is Future.Failure -> append("위치 정보를 불러오지 못했습니다")
                is Future.Loading, is Future.Nothing -> append("위치 정보를 가져오는 중입니다")
            }
        },
        onNavigate = onPlaceSelectorNavigate,
    ) {
        PlaceSelectorContent(
            onPlaceTypeSelect = { mainViewModel.setCurrentPlace(it, onPlaceChange) },
            onSelectOther = onPlaceSelectorNavigate,
            selectedPlaceType = mainViewModel.currentPlace.collectAsState().value.data?.type
                ?: PlaceType.CLASSROOM
        )
    }

    Spacer(Modifier.height(20.dp))

    ContentBox(
        title = "오늘의 급식",
        onNavigate = onMealPageSelectorNavigate,
        summary = buildAnnotatedString {
            when (mealTime) {
                is Future.Success -> {
                    append("우리 반의 $currentMealTypeByPage 급식 시간은 ")
                    withStyle(SpanStyle(color = Point)) {
                        append(
                            when (currentMealTypeByPage) {
                                "아침" -> mealTime._data.breakfastTime.asKorean12HoursString()
                                "점심" -> mealTime._data.lunchTime.asKorean12HoursString()
                                "저녁" -> mealTime._data.dinnerTime.asKorean12HoursString()
                                else -> ""
                            }
                        )
                    }
                    append("입니다")
                }
                is Future.Failure -> append("급식시간 정보를 불러오지 못했습니다")
                is Future.Loading, is Future.Nothing<*> -> append("급식시간 정보를 가져오는 중입니다")
            }
        }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .align(CenterHorizontally),
            horizontalArrangement = Arrangement.SpaceAround,
            ) {
            Column(
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.noRippleClickable {
                        pagerState.value = 0
                    },
                    text = "아침",
                    style = DTheme.typography.t3,
                    color = animateColorAsState(if (pagerState.value == 0) Point else DTheme.colors.c3).value,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(animateColorAsState(if (pagerState.value == 0) Point else Color.White).value)
                )
            }
            Column(
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.noRippleClickable {
                        pagerState.value = 1
                    },
                    text = "점심",
                    style = DTheme.typography.t3,
                    color = animateColorAsState(if (pagerState.value == 1) Point else DTheme.colors.c3).value,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(animateColorAsState(if (pagerState.value == 1) Point else Color.White).value)
                )
            }
            Column(
                horizontalAlignment = CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.noRippleClickable {
                        pagerState.value = 2
                    },
                    text = "저녁",
                    style = DTheme.typography.t3,
                    color = animateColorAsState(if (pagerState.value == 2) Point else DTheme.colors.c3).value,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(animateColorAsState(if (pagerState.value == 2) Point else Color.White).value)
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                when (todayMeal) {
                    is Future.Success -> append(
                        when (currentMealTypeByPage) {
                            "아침" -> todayMeal.data?.breakfast
                            "점심" -> todayMeal.data?.lunch
                            "저녁" -> todayMeal.data?.dinner
                            else -> ""
                        } ?: ""
                    )
                    is Future.Failure -> append("급식 정보를 불러오지 못했습니다")
                    is Future.Loading, is Future.Nothing<*> -> append("급식 정보를 가져오는 중입니다")
                }
            },
            style = DTheme.typography.mealMenu,
            color = DTheme.colors.c2,
            textAlign = if (todayMeal !is Future.Success) TextAlign.Center else null
        )

    }
}

@Composable
private fun PlaceSelectorContent(
    onPlaceTypeSelect: (PlaceType) -> Unit,
    onSelectOther: () -> Unit,
    selectedPlaceType: PlaceType,
) {
    val places =
        listOf(PlaceType.CLASSROOM, PlaceType.RESTROOM, PlaceType.CORRIDOR, PlaceType.TEACHER)

    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        places.forEach {
            SimplePlaceItem(
                placeType = it,
                onClick = {
                    onPlaceTypeSelect(it)
                },
                selected = selectedPlaceType == it,
            )
        }
        SimplePlaceItem(
            icon = R.drawable.ic_other_plus,
            name = "기타",
            onClick = onSelectOther,
            selected = false,
        )
    }
}

@Composable
private fun SimplePlaceItem(
    placeType: PlaceType,
    onClick: () -> Unit,
    selected: Boolean,
) {
    SimplePlaceItem(icon = placeType.icon,
        name = placeType.value,
        onClick = onClick,
        selected = selected)
}

@Composable
private fun SimplePlaceItem(
    @DrawableRes icon: Int,
    name: String,
    onClick: () -> Unit,
    selected: Boolean,
) {
    val color = animateColorAsState(if (selected) Point else DTheme.colors.c3)

    CompositionLocalProvider(LocalContentColor provides color.value) {
        Column(
            Modifier.noRippleClickable { if (!selected) onClick() },
            horizontalAlignment = CenterHorizontally,
        ) {
            val painter = painterResource(id = icon)
            Icon(
                modifier = Modifier.size(24.dp),
                painter = painter, contentDescription = null,
            )
            Spacer(Modifier.height(4.dp))
            Text(text = name, style = DTheme.typography.t6)
        }
    }
}
