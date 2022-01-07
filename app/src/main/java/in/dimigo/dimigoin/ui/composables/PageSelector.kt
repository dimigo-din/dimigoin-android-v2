package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C0
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun PageSelector(
    modifier: Modifier = Modifier,
    elements: List<String>,
    selected: Int,
    horizontalTextPadding: Dp = 10.dp,
    onChangeSelected: (Int) -> Unit,
) {
    var rowXPos by remember { mutableStateOf(0f) }
    var textWidth by remember { mutableStateOf(0) }
    val selectedWidth = with(LocalDensity.current) {
        (rowXPos * 2 + textWidth).toDp() - 8.dp
    }
    val offset = animateDpAsState(
        with(LocalDensity.current) {
            (textWidth * selected).toDp()
        }
    )
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .height(50.dp)
            .background(C4)
    ) {
        Spacer(
            modifier = Modifier
                .padding(4.dp)
                .width(selectedWidth)
                .offset(offset.value, 0.dp)
                .size(selectedWidth, 42.dp)
                .clip(RoundedCornerShape(21.dp))
                .background(MaterialTheme.colors.surface)
        )
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = horizontalTextPadding)
                .onGloballyPositioned {
                    rowXPos = it.positionInParent().x
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            elements.forEachIndexed { index, element ->
                val color = animateColorAsState(targetValue = if (selected == index) C0 else C2)
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .noRippleClickable {
                            onChangeSelected(index)
                        }
                        .onGloballyPositioned {
                            textWidth = it.size.width
                        },
                    text = element,
                    style = DTypography.t3,
                    color = color.value,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFF)
@Composable
fun PageSelectorPreview() {
    var selected by remember { mutableStateOf(0) }
    PageSelector(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
//        elements = listOf("월", "화", "수", "목", "금", "토", "일"),
//        elements = listOf("아침", "점심", "저녁"),
        elements = listOf("학급시간표", "학사일정"),
        selected = selected,
        horizontalTextPadding = 10.dp,
        onChangeSelected = { selected = it }
    )
}

private const val TAG = "PageSelector"
