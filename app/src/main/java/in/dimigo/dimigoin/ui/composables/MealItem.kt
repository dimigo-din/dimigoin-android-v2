package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.util.asKorean12HoursString
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import java.time.LocalTime
import java.util.*

@Composable
fun MealItem(
    modifier: Modifier = Modifier,
    type: MealTimeType,
    time: Optional<LocalTime>,
    meal: Optional<String>,
    onMealTimeClick: (MealTimeType) -> Unit,
    highlight: Boolean,
) = ConstraintLayout(
    modifier
        .clip(RoundedCornerShape(10.dp))
        .background(if (highlight) Point else Color.Transparent)
        .padding(vertical = 25.dp)
        .noRippleClickable { onMealTimeClick(type) }
) {
    val (typeText, timeText, mealText, startPad, endPad) = createRefs()
    val textColor = if (highlight) {
        MaterialTheme.colors.onPrimary
    } else {
        MaterialTheme.colors.onSurface
    }

    CompositionLocalProvider(LocalContentColor provides textColor) {
        Spacer(
            Modifier
                .width(if (highlight) 10.dp else 0.dp)
                .constrainAs(startPad) {
                    start.linkTo(parent.start)
                }
        )
        Spacer(
            Modifier
                .width(if (highlight) 10.dp else 0.dp)
                .constrainAs(endPad) {
                    end.linkTo(parent.end)
                }
        )
        Text(
            text = type.value,
            style = DTheme.typography.t2,
            modifier = Modifier.constrainAs(typeText) {
                top.linkTo(parent.top)
                start.linkTo(startPad.end, 20.dp)
            }
        )
        Text(
            text = time.map { it.asKorean12HoursString() }.orElse("시간 정보 없음") + "  >",
            style = DTheme.typography.t6,
            color = if (highlight) Color.White else Point,
            modifier = Modifier.constrainAs(timeText) {
                top.linkTo(typeText.top)
                bottom.linkTo(typeText.bottom)
                end.linkTo(endPad.start, 20.dp)
            }
        )
        Text(
            text = meal.orElse("급식 정보가 없습니다."),
            style = DTheme.typography.mealMenu,
            color = if (highlight) Color.White else DTheme.colors.c1,
            modifier = Modifier.constrainAs(mealText) {
                top.linkTo(typeText.bottom, 15.dp)
                start.linkTo(startPad.end, 20.dp)
                end.linkTo(endPad.start, 20.dp)
                width = Dimension.fillToConstraints
            }
        )
    }
}

enum class MealTimeType(
    val value: String,
    val integerValue: Int,
    val timeRange: ClosedRange<LocalTime>
) {
    BREAKFAST("아침", 0, LocalTime.of(6, 30)..LocalTime.of(8, 20)),
    LUNCH("점심", 1, LocalTime.of(8, 20)..LocalTime.of(13, 50)),
    DINNER("저녁", 2, LocalTime.of(13, 50)..LocalTime.of(19, 50))
}

@Preview
@Composable
fun MealItemPreview1() {
    MealItem(
        modifier = Modifier.fillMaxWidth(),
        type = MealTimeType.BREAKFAST,
        time = Optional.of(LocalTime.of(12, 0)),
        meal = Optional.of("현미밥 | 얼큰김칫국 | 토마토달걀볶음 | 호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | 스트링치즈 | 모닝빵미니버거"),
        highlight = false,
        onMealTimeClick = {}
    )
}

@Preview
@Composable
fun MealItemPreview2() {
    MealItem(
        modifier = Modifier.fillMaxWidth(),
        type = MealTimeType.BREAKFAST,
        time = Optional.of(LocalTime.of(7, 20)),
        meal = Optional.of("현미밥 | 얼큰김칫국 | 토마토달걀볶음 | 호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | 스트링치즈 | 모닝빵미니버거"),
        highlight = true,
        onMealTimeClick = {}
    )
}

@Preview
@Composable
fun MealItemPreview3() {
    Column(
        Modifier.padding(20.dp)
    ) {
        MealItem(
            modifier = Modifier.fillMaxWidth(),
            type = MealTimeType.BREAKFAST,
            time = Optional.of(LocalTime.of(7, 10)),
            meal = Optional.of("현미밥 | 얼큰김칫국 | 토마토달걀볶음 | \n호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | \n스트링치즈 | 모닝빵미니버거"),
            highlight = false,
            onMealTimeClick = {}
        )
        MealItem(
            modifier = Modifier.fillMaxWidth(),
            type = MealTimeType.LUNCH,
            time = Optional.of(LocalTime.of(12, 50)),
            meal = Optional.of("현미밥 | 얼큰김칫국 | 토마토달걀볶음 | \n호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | \n스트링치즈 | 모닝빵미니버거"),
            highlight = true,
            onMealTimeClick = {}
        )
        MealItem(
            modifier = Modifier.fillMaxWidth(),
            type = MealTimeType.DINNER,
            time = Optional.of(LocalTime.of(18, 40)),
            meal = Optional.of("현미밥 | 얼큰김칫국 | 토마토달걀볶음 | \n호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | \n스트링치즈 | 모닝빵미니버거"),
            highlight = false,
            onMealTimeClick = {}
        )
    }
}
