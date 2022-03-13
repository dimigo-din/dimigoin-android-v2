package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.ApplicationBox
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ApplicationScreen(
    gridPadding: Dp = 10.dp,
    onApplicationClick: (String) -> Unit,
) {
    Surface(Modifier.fillMaxHeight()) {
        Column(
            Modifier
                .fillMaxHeight()
                .statusBarsPadding()
                .padding(top = 36.dp)
        ) {
            Column(
                Modifier
                    .padding(horizontal = 20.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "", style = DTypography.t5, color = C2
                )
                Spacer(Modifier.height(5.dp))
                Text(
                    modifier = Modifier.padding(start = 15.dp),
                    text = "신청하기", style = DTypography.t0
                )
                Spacer(Modifier.height(24.dp))
                Column(
                    Modifier.align(Alignment.CenterHorizontally),
                    verticalArrangement = Arrangement.spacedBy(gridPadding)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(gridPadding),
                    ) {
                        ApplicationBox(
                            label = "세탁",
                            icon = R.drawable.ic_washing_machine,
                            iconTint = Color(0xFF838383),
                            onNavigate = { onApplicationClick("developing") }
                        )
                        ApplicationBox(
                            label = "기상송",
                            icon = R.drawable.ic_music_note,
                            iconTint = Color(0xFFE83C77),
                            onNavigate = { onApplicationClick("developing") }
                        )
                        ApplicationBox(
                            label = "주말잔류",
                            icon = R.drawable.ic_documents,
                            iconTint = Color(0xFF18BF7E),
                            onNavigate = { onApplicationClick("developing") }
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(gridPadding),
                    ) {
                        ApplicationBox(
                            label = "선밥/후밥",
                            icon = R.drawable.ic_tableware,
                            iconTint = Color(0xFF3C81E8),
                            onNavigate = { onApplicationClick("developing") }
                        )
                        ApplicationBox(
                            label = "특별실",
                            icon = R.drawable.ic_bulb,
                            iconTint = Color(0xFFFFB800),
                            onNavigate = { onApplicationClick("developing") }
                        )
                        ApplicationBox(
                            label = "금요귀가",
                            icon = R.drawable.ic_go_out,
                            iconTint = Color(0xFFED4455),
                            onNavigate = { onApplicationClick("developing") }
                        )
                    }
                }
            }
        }
    }
}
