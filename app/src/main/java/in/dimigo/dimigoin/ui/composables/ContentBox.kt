package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.theme.Shapes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Provides content box.
 *
 * @param modifier Modifiers to apply.
 * @param title The title to display.
 * @param summary Short description for the content. The summary is not displayed when null.
 * @param onNavigate `onNavigate` is called when the icon is touched. The icon is not displayed when null.
 * @param contentPadding The padding between header and contents. Only vertical padding affects the layout, defaults
 *                       to 25dp.
 * @param content The content to display.
 */
@Composable
fun ContentBox(
    modifier: Modifier = Modifier,
    title: String? = null,
    summary: AnnotatedString? = null,
    onNavigate: (() -> Unit)? = null,
    contentPadding: Dp = 25.dp,
    content: @Composable () -> Unit,
) = Card(
    modifier = modifier,
    shape = Shapes.medium,
    elevation = 0.dp
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 30.dp, vertical = 25.dp),
        verticalArrangement = Arrangement.spacedBy(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        title?.let { Header(title = title, onNavigate = onNavigate) }
        content()
        summary?.let { Summary(text = it) }
    }
}

@Composable
private fun Header(
    title: String,
    onNavigate: (() -> Unit)?,
) = Row(
    modifier = Modifier
        .fillMaxWidth()
        .then(
            onNavigate?.let { Modifier.noRippleClickable { onNavigate() } }
                ?: Modifier
        ),
    horizontalArrangement = Arrangement.SpaceBetween,
    verticalAlignment = Alignment.CenterVertically,
) {
    Text(
        text = title,
        style = DTypography.t2,
    )
    onNavigate?.let {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_right),
            contentDescription = stringResource(R.string.move_to_destination),
            tint = C3,
        )
    }
}

@Composable
private fun Summary(
    text: AnnotatedString,
) {
    Text(
        text = text,
        style = DTypography.t5,
    )
}

// region Previews
@Preview(showBackground = true,
    backgroundColor = 0xe5e5e5,
    name = "Content Box with navigation, summary")
@Composable
fun ContentBoxPreview1() {
    var text by remember { mutableStateOf("아무 것도 없어요... :(") }
    ContentBox(
        modifier = Modifier.padding(20.dp),
        title = "테스트",
        summary = buildAnnotatedString {
            append("테스트용 ")
            withStyle(SpanStyle(color = Point)) { append("메시지") }
            append("입니다.")
        },
        onNavigate = { text = "이제 있어요!" },
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true, backgroundColor = 0xe5e5e5, name = "Content Box with navigation")
@Composable
fun ContentBoxPreview2() {
    var text by remember { mutableStateOf("아무 것도 없어요... :(") }
    ContentBox(
        modifier = Modifier.padding(20.dp),
        title = "테스트",
        onNavigate = { text = "이제 있어요!" },
    ) {
        Text(text = text)
    }
}

@Preview(showBackground = true, backgroundColor = 0xe5e5e5, name = "Content Box with summary")
@Composable
fun ContentBoxPreview3() {
    ContentBox(
        modifier = Modifier.padding(20.dp),
        title = "테스트",
        summary = buildAnnotatedString {
            append("테스트용 ")
            withStyle(SpanStyle(color = Point)) { append("메시지") }
            append("입니다.")
        },
    ) {
        Text(text = "아무 것도 없어요!")
    }
}

@Preview(showBackground = true, backgroundColor = 0xe5e5e5, name = "Content Box with nothing")
@Composable
fun ContentBoxPreview4() {
    ContentBox(
        modifier = Modifier.padding(20.dp),
        title = "테스트",
    ) {
        Text(text = "아무 것도 없어요!")
    }
}

@Preview(showBackground = true, backgroundColor = 0xe5e5e5, name = "Content Box only with content")
@Composable
fun ContentBoxPreview5() {
    ContentBox(
        modifier = Modifier.padding(20.dp),
    ) {
        Text(text = "진짜 아무 것도 없어요!!!!")
    }
}
// endregion
