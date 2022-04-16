package `in`.dimigo.dimigoin.ui.theme

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BorderTextField(
    modifier: Modifier = Modifier,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    label: String,
    color: Color = DTheme.colors.c2,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
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
                Text(text = label, color = color, style = DTheme.typography.t5)
            }
            it()
        },
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation
    )
}

@Preview(backgroundColor = 0x000000)
@Composable
fun BorderTextFieldPrev() {
    BorderTextField(modifier = Modifier.padding(50.dp), value = TextFieldValue(), onValueChange = {}, label = "아이디를 입력하세요")
}