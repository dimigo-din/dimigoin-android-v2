package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.util.josa
import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ReasonScreen(
    modifier: Modifier = Modifier,
    place: Place,
    onConfirm: (place: Place, remark: String, context: Activity?) -> Unit,
    isFavoriteRegister: Boolean,
    onBackNavigation: (() -> Unit)?,
    activity: Activity? = LocalContext.current as Activity?
) {
    Box(modifier.padding(top = 26.dp)) {
        val (reason, setReason) = remember { mutableStateOf("") }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight(),
        ) {
            if (onBackNavigation != null) {
                PlaceSelectorTopBar(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    title = "",
                    onBackNavigation = onBackNavigation,
                    showSearchIcon = false,
                    onSearch = { },
                    color = Color.Black
                )
            }
            Spacer(Modifier.weight(.5f))
            if (isFavoriteRegister) {
                AddFavoriteItem(place = place, reason = reason, onReasonChange = setReason)
            } else {
                ChangePlaceItem(place = place, reason = reason, onReasonChange = setReason)
            }
            Spacer(Modifier.height(60.dp))

            val background = if (reason.isBlank()) {
                Point.copy(alpha = 0.5f)
            } else {
                Point
            }

            val clickableModifier = if (reason.isBlank()) {
                Modifier
            } else {
                Modifier.clickable { onConfirm(place, reason) }
            }

            Box(
                modifier = Modifier
                    .height(58.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(background)
                    .then(clickableModifier)
                ,
                contentAlignment = Alignment.Center
            ) {
                Text(text = "등록", style = DTypography.t3, color = MaterialTheme.colors.onPrimary)
            }
            Spacer(Modifier.weight(.5f))
        }
    }
}

@Composable
private fun ChangePlaceItem(
    place: Place,
    reason: String,
    onReasonChange: (String) -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        text = buildAnnotatedString {
            append("나의 위치를 ")
            withStyle(SpanStyle(color = Point)) { append(place.name) }
            append(place.name.josa("으로", true))
            append(" 이동합니다")
        },
        color = MaterialTheme.colors.onSurface,
        style = DTypography.t4,
    )
    Spacer(Modifier.height(15.dp))
    Text(
        text = "위치 이동 사유를 등록해주세요",
        style = DTypography.pageSubtitle,
    )
    Spacer(Modifier.height(60.dp))

    val focusRequester = remember { FocusRequester() }
    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .focusRequester(focusRequester),
        value = reason, onValueChange = onReasonChange,
        placeholder = {
            Text("10글자 이하로 입력해주세요", style = DTypography.t4, color = C3)
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun AddFavoriteItem(
    place: Place,
    reason: String,
    onReasonChange: (String) -> Unit,
) = Column(
    horizontalAlignment = Alignment.CenterHorizontally,
) {
    Text(
        text = buildAnnotatedString {
            withStyle(SpanStyle(color = Point)) { append(place.name) }
            append(place.name.josa("을를", true))
            append(" 즐겨찾기에 추가합니다")
        },
        color = MaterialTheme.colors.onSurface,
        style = DTypography.t4,
    )
    Spacer(Modifier.height(15.dp))
    Text(
        text = "별을 다시 누르면 즐겨찾기가 삭제됩니다",
        style = DTypography.pageSubtitle,
    )
    Spacer(Modifier.height(60.dp))

    val focusRequester = remember { FocusRequester() }
    CustomTextField(
        modifier = Modifier
            .padding(horizontal = 40.dp)
            .focusRequester(focusRequester),
        value = reason, onValueChange = onReasonChange,
        placeholder = {
            Text("10글자 이하로 이동 사유를 입력해주세요", style = DTypography.t4, color = C3)
        }
    )
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value, onValueChange = onValueChange,
        textStyle = DTypography.t4.copy(
            textAlign = if (value.isEmpty()) TextAlign.Start else TextAlign.Center,
            color = MaterialTheme.colors.onSurface,
        ),
        cursorBrush = SolidColor(Point),
        modifier = modifier.onFocusChanged {
            focused = it.isFocused
        },
    ) { innerTextField ->
        Box(
            Modifier.height(30.dp)
        ) {
            Box(Modifier.align(Alignment.TopCenter)) {
                if (value.isEmpty()) {
                    placeholder()
                }
                innerTextField()
            }
            Divider(
                modifier = Modifier.align(Alignment.BottomCenter),
                color = if (focused) Point else C3, thickness = 2.dp
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun ReasonScreenPreview1() {
    ReasonScreen(
        place = Place("", "집", "", "", "", "", PlaceType.ETC),
        onConfirm = { _, _, _ -> },
        isFavoriteRegister = false,
        onBackNavigation = { }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun ReasonScreenPreview2() {
    ReasonScreen(
        place = Place("", "집", "", "", "", "", PlaceType.ETC),
        onConfirm = { _, _, _ -> },
        isFavoriteRegister = true,
        onBackNavigation = { }
    )
}
