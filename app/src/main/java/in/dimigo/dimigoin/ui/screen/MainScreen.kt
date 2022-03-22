package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.ui.composables.ContentBox
import `in`.dimigo.dimigoin.ui.composables.MealTimeType
import `in`.dimigo.dimigoin.ui.composables.OnLifecycleEvent
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.ui.util.asKorean12HoursString
import `in`.dimigo.dimigoin.ui.util.icon
import `in`.dimigo.dimigoin.viewmodel.MainViewModel
import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
    val todayMeal = mainViewModel.todayMeal.collectAsState().value.data
    val pagerState = remember { mutableStateOf(mainViewModel.getCurrentMealType()) }
    val currentMealTypeByPage = when (pagerState.value) {
        0 -> MealTimeType.BREAKFAST.value
        1 -> MealTimeType.LUNCH.value
        2 -> MealTimeType.DINNER.value
        else -> ""
    }
    val coroutineScope = rememberCoroutineScope()

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
                    style = DTypography.t3,
                    color = if (pagerState.value == 0) Point else C3,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(if (pagerState.value == 0) Point else Color.White)
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
                    style = DTypography.t3,
                    color = if (pagerState.value == 1) Point else C3,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(if (pagerState.value == 1) Point else Color.White)
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
                    style = DTypography.t3,
                    color = if (pagerState.value == 2) Point else C3,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(5.dp))
                Spacer(
                    modifier = Modifier
                        .size(4.dp)
                        .clip(RoundedCornerShape(100))
                        .background(if (pagerState.value == 2) Point else Color.White)
                )
            }
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = buildAnnotatedString {
                when (mealTime) {
                    is Future.Success -> append(
                        when (currentMealTypeByPage) {
                            "아침" -> todayMeal?.breakfast
                            "점심" -> todayMeal?.lunch
                            "저녁" -> todayMeal?.dinner
                            else -> ""
                        } ?: ""
                    )
                    is Future.Failure -> append("급식 정보를 불러오지 못했습니다")
                    is Future.Loading, is Future.Nothing<*> -> append("급식 정보를 가져오는 중입니다")
                }
            },
            style = DTypography.mealMenu,
            color = C2,
            textAlign = if (mealTime !is Future.Success) TextAlign.Center else null
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
    val color = animateColorAsState(if (selected) Point else C3)

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
            Text(text = name, style = DTypography.t6)
        }
    }
}
