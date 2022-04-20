package `in`.dimigo.dimigoin.ui.composables

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalTime

@Composable
fun MealTimeItem(
    modifier: Modifier = Modifier,
    order: Int,
    grade: Int,
    `class`: Int,
    time: LocalTime,
    highlight: Boolean,
) = Row(
    modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(10.dp))
        .background(if (highlight) Point else Color.Transparent)
        .padding(vertical = 15.dp)
        .padding(start = 30.dp, end = 35.dp),
    verticalAlignment = Alignment.CenterVertically,
) {
    val textColor = if (highlight) Color.White else MaterialTheme.colors.onSurface
    CompositionLocalProvider(LocalContentColor provides textColor) {
        Box {
            Text(text = "${order}순위", style = DTheme.typography.t6)
            // for fixed-width
            Text(text = "0순위", style = DTheme.typography.t6, color = Color.Transparent)
        }
        Spacer(Modifier.width(20.dp))
        Text(text = "${grade}학년 $`class`반", style = DTheme.typography.t4)
        Spacer(Modifier.weight(1f))
        if (highlight) {
            Text(text = time.asKorean12HoursString(), style = DTheme.typography.t5)
        } else {
            Text(text = time.asKorean12HoursString(), style = DTheme.typography.t5.copy(fontWeight = FontWeight.Medium))
        }
    }
}

@Preview
@Composable
fun MealTimePreview1() {
    MealTimeItem(order = 1, grade = 2, `class` = 6, time = LocalTime.of(12, 50), highlight = false)
}

@Preview
@Composable
fun MealTimePreview2() {
    MealTimeItem(order = 1, grade = 2, `class` = 6, time = LocalTime.of(12, 50), highlight = true)
}
