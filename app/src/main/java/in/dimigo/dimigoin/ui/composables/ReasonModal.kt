package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Shapes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ReasonModal(
    place: Place,
    onConfirm: (place: Place, remark: String) -> Unit,
    isFavoriteRegister: Boolean,
) {
    Surface(shape = Shapes.large) {
        var reason by remember { mutableStateOf("") }
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = "'${place.name}' ${if (isFavoriteRegister) "즐겨찾기 등록" else "이동"} 사유 입력",
                style = DTypography.t4,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BasicTextField(modifier = Modifier.background(C4), value = reason, onValueChange = { reason = it })
                Button(onClick = { onConfirm(place, reason) }) {
                    Text("확인", style = DTypography.t5)
                }
            }
            if (isFavoriteRegister) {
                Text(
                    text = "별을 한 번 더 누르면, 즐겨찾기가 삭제됩니다.",
                    style = DTypography.t5,
                )
            }
        }
    }
}