package `in`.dimigo.dimigoin.ui.screen.meal

import `in`.dimigo.dimigoin.ui.composables.MealItem
import `in`.dimigo.dimigoin.ui.composables.MealTimeType
import `in`.dimigo.dimigoin.ui.composables.PageSelector
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.util.asKoreanWeekString
import `in`.dimigo.dimigoin.viewmodel.MealViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MealScreen(
    mealViewModel: MealViewModel = getViewModel(),
    onMealTimeClick: (MealTimeType) -> Unit,
) {
    val mealTime = mealViewModel.mealTime.collectAsState().value
    val weeklyMeal = mealViewModel.weeklyMeal.collectAsState().value
    val dayOfWeek = LocalDate.now().dayOfWeek.value
    val timeNow = LocalTime.now()
    val pagerState = rememberPagerState(dayOfWeek - 1)
    val coroutineScope = rememberCoroutineScope()

    Surface(Modifier.fillMaxHeight()) {
        Column(
            Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(top = 36.dp)
        ) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
                    .wrapContentHeight()
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = LocalDate.now().asKoreanWeekString(), style = DTypography.t5, color = C2
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "주간 급식표", style = DTypography.t0
                )
                Spacer(Modifier.height(24.dp))
                PageSelector(
                    elements = listOf("월", "화", "수", "목", "금", "토", "일"),
                    pagerState = pagerState,
                    onChangeSelected = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    },
                )
                Spacer(Modifier.height(10.dp))
            }

            HorizontalPager(
                modifier = Modifier.weight(1f),
                count = 7,
                state = pagerState,
                itemSpacing = 20.dp,
            ) { page ->
                val scrollState = rememberScrollState()

                Column(
                    Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState)
                ) {
                    val isToday = dayOfWeek == page + 1
                    MealItem(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        type = MealTimeType.BREAKFAST,
                        time = mealTime.data?.breakfastTime,
                        meal = weeklyMeal.data?.get(page)?.breakfast,
                        onMealTimeClick = onMealTimeClick,
                        highlight = timeNow.isAfter(LocalTime.of(6, 30)) &&
                                timeNow.isBefore(LocalTime.of(8, 20)) &&
                                isToday,
                    )
                    MealItem(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        type = MealTimeType.LUNCH,
                        time = mealTime.data?.lunchTime,
                        meal = weeklyMeal.data?.get(page)?.lunch,
                        onMealTimeClick = onMealTimeClick,
                        highlight = timeNow.isAfter(LocalTime.of(8, 20)) &&
                                timeNow.isBefore(LocalTime.of(13, 50)) &&
                                isToday,
                    )
                    MealItem(
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .fillMaxWidth(),
                        type = MealTimeType.DINNER,
                        time = mealTime.data?.dinnerTime,
                        meal = weeklyMeal.data?.get(page)?.dinner,
                        onMealTimeClick = onMealTimeClick,
                        highlight = timeNow.isAfter(LocalTime.of(13, 50)) &&
                                timeNow.isBefore(LocalTime.of(19, 50)) &&
                                isToday,
                    )
                    Spacer(Modifier.height(10.dp))
                }
            }

            Spacer(modifier = Modifier.navigationBarsHeight(60.dp))
        }
    }
}
