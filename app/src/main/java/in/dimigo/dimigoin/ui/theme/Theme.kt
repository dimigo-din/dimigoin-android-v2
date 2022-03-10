package `in`.dimigo.dimigoin.ui.theme

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Point,
    surface = Color(0xFF222222),
    onPrimary = Color.White,
    error = Red,
)

private val LightColorPalette = lightColors(
    primary = Point,
    background = C4,
    onBackground = C2,
    surface = Color.White,
    onPrimary = Color.White,
    onSurface = Color.Black,
    error = Red,
)

@Composable
fun DimigoinTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(Color.Transparent)

    MaterialTheme(
        colors = colors,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun BorderTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    color: Color = C2,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
) {
    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = color, shape = RoundedCornerShape(11.dp))
            .padding(vertical = 15.dp, horizontal = 25.dp),
        value = value,
        onValueChange = onValueChange,
        textStyle = TextStyle.Default.copy(fontSize = 16.sp),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = keyboardOptions,
        decorationBox = {
            if (value.text.isEmpty()) {
                Text(text = label, color = color, style = DTypography.t5)
            }
            it()
        },
        visualTransformation = visualTransformation
    )
}

@Preview(backgroundColor = 0x000000)
@Composable
fun BorderTextFieldPrev() {
    BorderTextField(modifier = Modifier.padding(50.dp), value = TextFieldValue(), onValueChange = {}, label = "아이디를 입력하세요")
}