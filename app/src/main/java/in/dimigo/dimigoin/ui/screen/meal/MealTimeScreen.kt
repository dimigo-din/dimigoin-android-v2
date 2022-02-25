package `in`.dimigo.dimigoin.ui.screen.meal

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.meal.MealTime
import `in`.dimigo.dimigoin.ui.composables.MealTimeItem
import `in`.dimigo.dimigoin.ui.composables.PageSelector
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.viewmodel.MealTimeViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import java.time.LocalTime
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel

private const val BREAKFAST = 0
private const val LUNCH = 1
private const val DINNER = 2

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MealTimeScreen(
    viewModel: MealTimeViewModel = getViewModel(),
    startPage: Int,
    onBackPress: () -> Unit,
) {
    val mealTimes = viewModel.mealTimes.collectAsState().value
    val user = viewModel.me.collectAsState().value.data ?: return

    val pagerState = rememberPagerState(startPage)
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    Surface(
        Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(top = 36.dp)
        ) {
            Column(Modifier.padding(horizontal = 20.dp)) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "급식시간 제공: 달그락", style = DTypography.t5, color = C2
                )
                Spacer(Modifier.height(5.dp))
                Row(
                    modifier = Modifier.padding(start = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "학급별 급식시간", style = DTypography.t0)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close), contentDescription = null,
                        tint = C2,
                        modifier = Modifier
                            .padding(vertical = 10.dp, horizontal = 15.dp)
                            .noRippleClickable { onBackPress() }
                    )
                }
                Spacer(Modifier.height(24.dp))
                PageSelector(
                    elements = listOf("아침", "점심", "저녁"),
                    selected = pagerState.currentPage,
                    onChangeSelected = {
                        coroutineScope.launch {
                            pagerState.scrollToPage(it)
                        }
                    },
                )
                Spacer(Modifier.height(19.dp))
            }

            HorizontalPager(
                count = 3,
                state = pagerState,
                contentPadding = PaddingValues(horizontal = 20.dp),
                itemSpacing = 20.dp,
            ) {
                Column(
                    Modifier
                        .fillMaxHeight()
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {
                    mealTimes.data?.sortedBy {
                        getRankByPage(it, currentPage)
                    }?.forEach {
                        MealTimeItem(
                            order = getRankByPage(it, currentPage),
                            grade = it.grade,
                            `class` = it.`class`,
                            time = getTimeByPage(it, currentPage),
                            highlight = it.`class` == user.`class`
                        )
                    }
                }
            }
        }
    }
}

fun getRankByPage(mealTime: MealTime, page: Int) =
    when (page) {
        LUNCH -> mealTime.lunchRank
        DINNER -> mealTime.dinnerRank
        else -> 1
    }

fun getTimeByPage(mealTime: MealTime, page: Int) =
    when (page) {
        BREAKFAST -> mealTime.breakfastTime
        LUNCH -> mealTime.lunchTime
        DINNER -> mealTime.dinnerTime
        else -> LocalTime.of(0, 0)
    }
