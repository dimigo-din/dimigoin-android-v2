package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C0
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlin.math.roundToInt

@OptIn(ExperimentalPagerApi::class)
@Composable
fun PageSelector(
    modifier: Modifier = Modifier,
    elements: List<String>,
    pagerState: PagerState,
    horizontalTextPadding: Dp = 10.dp,
    onChangeSelected: (Int) -> Unit,
) {
    var rowXPos by remember { mutableStateOf(0f) }
    var textWidth by remember { mutableStateOf(0f) }
    val selectedWidth = with(LocalDensity.current) {
        (rowXPos * 2 + textWidth).toDp() - 8.dp
    }
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(25.dp))
            .height(50.dp)
            .background(C4)
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .width(selectedWidth)
                .offset {
                    val scrollPosition = (pagerState.currentPage + pagerState.currentPageOffset)
                        .coerceIn(0f,
                            (pagerState.pageCount - 1)
                                .coerceAtLeast(0)
                                .toFloat()
                        )
                    IntOffset(
                        x = (textWidth * scrollPosition).toInt(),
                        y = 0
                    )
                }
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
                    textWidth = (it.size.width.toFloat() / elements.size)
                },
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            elements.forEachIndexed { index, element ->
                val color = animateColorAsState(
                    if ((pagerState.currentPage + pagerState.currentPageOffset).roundToInt() == index) {
                        C0
                    } else {
                        C2
                    }
                )
                Box(
                    Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .noRippleClickable {
                            onChangeSelected(index)
                        }
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = element,
                        style = DTypography.t3,
                        color = color.value,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true, backgroundColor = 0xFFFFFFF)
@Composable
fun PageSelectorPreview() {
    Column(Modifier.fillMaxWidth()) {
        var selected1 by remember { mutableStateOf(0) }
        PageSelector(
            modifier = Modifier.fillMaxWidth(),
            elements = listOf("월", "화", "수", "목", "금", "토", "일"),
            pagerState = rememberPagerState(),
            horizontalTextPadding = 10.dp,
            onChangeSelected = { selected1 = it }
        )
        var selected2 by remember { mutableStateOf(0) }
        PageSelector(
            modifier = Modifier.fillMaxWidth(),
            elements = listOf("아침", "점심", "저녁"),
            pagerState = rememberPagerState(),
            horizontalTextPadding = 10.dp,
            onChangeSelected = { selected2 = it }
        )
        var selected3 by remember { mutableStateOf(0) }
        PageSelector(
            modifier = Modifier.fillMaxWidth(),
            elements = listOf("학급시간표", "학사일정"),
            pagerState = rememberPagerState(),
            horizontalTextPadding = 10.dp,
            onChangeSelected = { selected3 = it }
        )
    }
}

private const val TAG = "PageSelector"
