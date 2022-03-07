package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Shapes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun ApplicationBox(
    modifier: Modifier = Modifier,
    label: String,
    icon: Int,
    onNavigate: (() -> Unit)? = null,
) = Card(
    modifier = modifier,
    shape = Shapes.medium,
    elevation = 0.dp
) {
    Surface(
        modifier = Modifier
            .then(onNavigate?.let { Modifier.noRippleClickable { onNavigate() } }
            ?: Modifier)
    ){
        Column(
            modifier = Modifier
                .background(Color(0xFFFAFAFC))
                .padding(horizontal = 40.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = icon), contentDescription = null,
                tint = C3,
            )
            Text(text = label, style = DTypography.t3)
        }
    }
}
