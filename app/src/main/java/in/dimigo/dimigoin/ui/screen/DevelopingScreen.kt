package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 35.dp, horizontal = 20.dp)
    ) {
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 240.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "해당 기능은" +
                        "\n" +
                        "아직 개발중입니다",
                style = DTypography.t2.copy(fontSize = 20.sp),
                textAlign = TextAlign.Center,
                fontWeight = Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "추후 업데이트 될 예정입니다",
                style = DTypography.explainText,
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Point, shape = RoundedCornerShape(30))
                .padding(vertical = 16.dp)
                .clickable { backOnClick() },
            textAlign = TextAlign.Center,
            text = "돌아가기",
            style = DTypography.t3,
            fontWeight = Bold,
            color = Color.White,
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun DevelopingPrev() {
    DevelopingScreen(modifier = Modifier, backOnClick = {})
}