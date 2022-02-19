package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.MonthlySchedule
import `in`.dimigo.dimigoin.domain.entity.Schedule
import `in`.dimigo.dimigoin.domain.entity.ScheduleType
import `in`.dimigo.dimigoin.ui.composables.PageSelector
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C1
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.LocalOverScrollConfiguration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun ScheduleScreen() {

    val monthlySchedule = listOf(
        Schedule(ScheduleType.EVENT, LocalDate.of(2022, 3, 2), "개학식"),
        Schedule(ScheduleType.HOME, LocalDate.of(2022, 3, 4), "전체귀가"),
        Schedule(ScheduleType.JAIL, LocalDate.of(2022, 3, 5), "전체잔류"),
        Schedule(ScheduleType.JAIL, LocalDate.of(2022, 3, 6), "전체잔류"),
        Schedule(ScheduleType.EXAM, LocalDate.of(2022, 3, 28), "1학기 3차 지필평가"),
        Schedule(ScheduleType.EVENT, LocalDate.of(2022, 3, 28), "방학식"),
        Schedule(ScheduleType.VACATION, LocalDate.of(2022, 3, 29), "방학"),
        Schedule(ScheduleType.VACATION, LocalDate.of(2022, 3, 30), "방학"),
        Schedule(ScheduleType.VACATION, LocalDate.of(2022, 3, 31), "방학"),
    )

    var page by remember { mutableStateOf(0) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    Surface(
        Modifier
            .fillMaxHeight()
            .statusBarsPadding()
    ) {
        Column(
            Modifier
                .fillMaxHeight()
                .padding(top = 36.dp)
                .padding(horizontal = 20.dp)
        ) {
            when (page) {
                0 -> TimetableHeader()
                1 -> SchoolScheduleHeader(
                    selectedDate,
                    onDateChange = { selectedDate = it }
                )
            }
            Spacer(Modifier.height(24.dp))
            PageSelector(
                elements = listOf("학급시간표", "학사일정"),
                selected = page,
                onChangeSelected = { page = it },
            )
            Spacer(Modifier.height(33.dp))
            when (page) {
                0 -> Timetable()
                1 -> SchoolSchedule(
                    selectedDate = selectedDate,
                    onDateSelect = { selectedDate = it },
                    today = LocalDate.of(2022, 3, 28),
                    schedules = monthlySchedule,
                )
            }
        }
    }
}

@Composable
fun TimetableHeader() {

}

@Composable
fun Timetable() {

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
            painter = painterResource(id = R.drawable.ic_round_arrow_left), contentDescription = null,
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
            painter = painterResource(id = R.drawable.ic_round_arrow_right), contentDescription = null,
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
                            scheduleColors = schedules.filter { it.date == date }.map { it.type.color },
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
