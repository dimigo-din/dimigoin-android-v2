package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DevelopingScreen(
    modifier: Modifier = Modifier,
    backOnClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            Modifier
                .systemBarsPadding()
                .padding(vertical = 35.dp, horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {
            Spacer(Modifier.weight(1f))
            Text(
                text = "해당 기능은" +
                        "\n" +
                        "아직 개발중입니다",
                style = DTheme.typography.t2.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                fontWeight = Bold,
                color = MaterialTheme.colors.onSurface
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "추후 업데이트 될 예정입니다",
                style = DTheme.typography.explainText,
                color = DTheme.colors.c2,
            )
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = { backOnClick() },
                shape = RoundedCornerShape(30),
                colors = ButtonDefaults.buttonColors(contentColor = Point)
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    textAlign = TextAlign.Center,
                    text = "돌아가기",
                    style = DTheme.typography.t4,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DevelopingPrev() {
    DevelopingScreen(modifier = Modifier, backOnClick = {})
}