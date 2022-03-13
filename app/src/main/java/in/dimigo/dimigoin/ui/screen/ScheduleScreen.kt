package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.schedule.DailyTimetable
import `in`.dimigo.dimigoin.domain.entity.schedule.MonthlySchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.Schedule
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.ui.composables.PageSelector
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.*
import `in`.dimigo.dimigoin.viewmodel.ScheduleViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale
import java.time.DayOfWeek

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = getViewModel(),
) {

    val timetable =
        viewModel.timetable.collectAsState().value.data ?: List(5) {
            DailyTimetable(
                emptyList(),
                LocalDate.now()
            )
        }
    val annualSchedule = viewModel.schedule.collectAsState().value.data ?: mapOf()
    val user = viewModel.me.collectAsState().value.data ?: return

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Surface(
        Modifier
            .fillMaxHeight()
    ) {
        HorizontalPager(count = 2, state = pagerState, userScrollEnabled = false) { }
        Column(
            Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(top = 36.dp, bottom = 60.dp)
                .padding(horizontal = 20.dp)
        ) {
            when (pagerState.currentPage) {
                0 -> TimetableHeader(user)
                1 -> SchoolScheduleHeader(
                    selectedDate,
                    onDateChange = { selectedDate = it }
                )
            }
            Spacer(Modifier.height(24.dp))
            PageSelector(
                elements = listOf("학급시간표", "학사일정"),
                pagerState = pagerState,
                onChangeSelected = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
            )
            Spacer(Modifier.height(33.dp))
            when (pagerState.currentPage) {
                0 -> Timetable(timetable)
                1 -> SchoolSchedule(
                    selectedDate = selectedDate,
                    onDateSelect = { selectedDate = it },
                    today = LocalDate.now(),
                    schedules = getNearSchedules(annualSchedule, YearMonth.from(selectedDate)),
                )
            }
        }
    }
}

fun getNearSchedules(
    schedule: Map<YearMonth, List<Schedule>>,
    yearMonth: YearMonth
): List<Schedule> =
    schedule.getOrDefault(yearMonth.minusMonths(1), emptyList()) +
            schedule.getOrDefault(yearMonth, emptyList()) +
            schedule.getOrDefault(yearMonth.plusMonths(1), emptyList())

@Composable
fun TimetableHeader(user: User) {
    val date = LocalDate.now()
    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = "${date.minusMonths(2).year}학년도 " +
                when (date.monthValue) {
                    in 3..7 -> "1학기"
                    else -> "2학기"
                } + " ${user.grade}학년 ${user.`class`}반",
        style = DTypography.t5, color = C2
    )
    Spacer(Modifier.height(5.dp))
    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = "학급시간표", style = DTypography.t0
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Timetable(weeklyTimetable: WeeklyTimetable) {
    if (weeklyTimetable.all { it.sequence.isEmpty() }) {
        TimetableContentEmpty()
        return
    }

    val today = LocalDate.now().dayOfWeek
    Column(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TimetableDayHeader(today)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp)
                .background(MaterialTheme.colors.surface)
        )
        Divider(color = C4)
        CompositionLocalProvider(LocalOverScrollConfiguration provides null) {
            Column(
                Modifier.verticalScroll(rememberScrollState())
            ) {
                Spacer(Modifier.height(20.dp))
                TimetableContent(weeklyTimetable)
            }
        }
    }
}

@Composable
fun TimetableContent(weeklyTimetable: WeeklyTimetable) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        weeklyTimetable.forEach { dailyTimetable ->
            DailyTimetableContent(dailyTimetable)
        }
    }
}

@Composable
fun DailyTimetableContent(dailyTimetable: DailyTimetable) {
    Column(
        modifier = Modifier.padding(bottom = 15.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        val date = dailyTimetable.date
        val time = LocalTime.now()
        val isToday = LocalDate.now().equals(date)

        operator fun LocalTime.rangeTo(other: LocalTime) = this to other
        operator fun Pair<LocalTime, LocalTime>.contains(value: LocalTime) =
            value.isAfter(first) && value.isBefore(second)

        dailyTimetable.sequence.forEachIndexed { index, name ->
            ClassItem(
                name = name ?: "",
                today = isToday,
                highlight = isToday && isPeriodInTime(index + 1, time)
            )
        }
    }
}

@Composable
fun TimetableDayHeader(today: DayOfWeek) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface),
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        "월화수목금".forEachIndexed { index, day ->
            Text(
                text = day.toString(),
                style = DTypography.t3,
                color = if (index + 1 == today.value) MaterialTheme.colors.onSurface else C2,
            )
        }
    }
}

fun isPeriodInTime(period: Int, time: LocalTime): Boolean =
    when (period) {
        1 -> time in LocalTime.of(6, 30)..LocalTime.of(9, 50)
        2 -> time in LocalTime.of(9, 50)..LocalTime.of(10, 50)
        3 -> time in LocalTime.of(10, 50)..LocalTime.of(11, 50)
        4 -> time in LocalTime.of(11, 50)..LocalTime.of(12, 50)
        5 -> time in LocalTime.of(12, 50)..LocalTime.of(14, 40)
        6 -> time in LocalTime.of(14, 40)..LocalTime.of(15, 40)
        7 -> time in LocalTime.of(15, 40)..LocalTime.of(16, 40)
        else -> false
    }

@Composable
fun TimetableContentEmpty() {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Text(text = "시간표 정보가 없습니다", style = DTypography.t4, color = C1)
    }
}

@Preview
@Composable
fun TimetablePreview() {
    Timetable(
        weeklyTimetable = List(5) {
            DailyTimetable(List(20) { "데프" }, LocalDate.of(1970, 1, 1))
        }
    )
}

@Composable
fun ClassItem(
    name: String,
    today: Boolean,
    highlight: Boolean,
) {
    Box(
        modifier = Modifier
            .size(66.dp, 35.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (highlight) Point else Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            textAlign = TextAlign.Center,
            color = if (highlight) Color.White else if (today) MaterialTheme.colors.onSurface else C2,
            style = DTypography.t4
        )
    }
}

@Composable
fun SchoolScheduleHeader(
    selectedDate: LocalDate,
    onDateChange: (LocalDate) -> Unit,
) {
    Text(
        modifier = Modifier.padding(start = 15.dp),
        text = "${selectedDate.minusMonths(2).year}학년도",
        style = DTypography.t5, color = C2
    )
    Spacer(Modifier.height(5.dp))
    Row(
        modifier = Modifier.padding(start = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "학사일정", style = DTypography.t0)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            painter = painterResource(id = R.drawable.ic_round_arrow_left),
            contentDescription = null,
            tint = C2,
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .noRippleClickable { onDateChange(selectedDate.minusMonths(1)) }
        )
        Text(
            text = "${selectedDate.monthValue}월",
            style = DTypography.t2, color = C2,
            textAlign = TextAlign.Center,
            modifier = Modifier.widthIn(min = 50.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_round_arrow_right),
            contentDescription = null,
            tint = C2,
            modifier = Modifier
                .padding(vertical = 10.dp, horizontal = 15.dp)
                .noRippleClickable { onDateChange(selectedDate.plusMonths(1)) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SchoolSchedule(
    selectedDate: LocalDate,
    onDateSelect: (LocalDate) -> Unit,
    today: LocalDate = LocalDate.now(),
    schedules: MonthlySchedule,
) {
    val dailyScheduleMap = schedules.groupBy { it.date }

    val yearMonth = YearMonth.from(selectedDate)

    val firstDay = yearMonth.atDay(1)
    val lastDay = yearMonth.atEndOfMonth()

    val firstDisplayDay = firstDay.minusDays(firstDay.dayOfWeek.value % 7L) // 일요일
    val lastDisplayDay = lastDay.plusDays(6L - lastDay.dayOfWeek.value % 7L) // 토요일

    val diff = ChronoUnit.DAYS.between(firstDisplayDay, lastDisplayDay).toInt() + 1

    CompositionLocalProvider(LocalOverScrollConfiguration provides null) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    "일월화수목금토".forEach { day ->
                        Text(
                            text = day.toString(),
                            style = DTypography.t5,
                            color = C2,
                        )
                    }
                }
                Spacer(
                    modifier = Modifier
                        .height(22.dp)
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                )
                Divider(color = C4)
                Spacer(Modifier.height(7.dp))
            }

            items(
                List(diff) { days ->
                    firstDisplayDay.plusDays(days.toLong())
                }.windowed(7, 7)
            ) { week ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(47.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    week.forEach { date ->
                        DateItem(
                            month = yearMonth.monthValue,
                            date = date,
                            selected = date.equals(selectedDate),
                            today = date.equals(today),
                            scheduleColors = schedules.filter { it.date == date }
                                .map { it.type.color },
                            onSelect = { onDateSelect(date) }
                        )
                    }
                }
            }

            item { Divider(modifier = Modifier.padding(vertical = 7.dp), color = C4) }

            if (dailyScheduleMap.containsKey(selectedDate)) {
                items(dailyScheduleMap.getOrDefault(selectedDate, emptyList())) {
                    ScheduleItem(modifier = Modifier.fillMaxWidth(), schedule = it)
                }
                item { /* Empty space */ }
            }
        }
    }
}

@Composable
fun DateItem(
    month: Int,
    date: LocalDate,
    selected: Boolean,
    today: Boolean,
    scheduleColors: List<Long>,
    onSelect: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(32.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (today) Point else if (selected) C3 else Color.Transparent)
            .noRippleClickable { onSelect() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = DTypography.t5,
            color = if (today) Color.White else if (selected) C1 else if (month == date.monthValue) C2 else C3,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        if (scheduleColors.isNotEmpty()) {
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                scheduleColors.forEach { scheduleColor ->
                    Spacer(
                        Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(Color(scheduleColor))
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun ScheduleItem(
    modifier: Modifier = Modifier,
    schedule: Schedule,
) {
    Row(
        modifier,
        horizontalArrangement = Arrangement.spacedBy(22.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            Modifier
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(schedule.type.color))
        )
        Column(
            modifier = Modifier.widthIn(min = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = schedule.date.dayOfMonth.toString().padStart(2, '0'),
                color = C1, style = DTypography.t3
            )
            Text(
                text = schedule.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREAN),
                color = C1, style = DTypography.t5
            )
        }
        Text(
            text = schedule.name,
            color = MaterialTheme.colors.onSurface, style = DTypography.t4
        )
    }
}
