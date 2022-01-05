package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.DTypography
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun PlaceSelectorTopBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackNavigation: () -> Unit,
    showSearchIcon: Boolean,
    onSearch: () -> Unit,
    color: Color,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CompositionLocalProvider(LocalContentColor provides color) {
            Icon(
                modifier = Modifier
                    .padding(end = 15.dp)
                    .noRippleClickable { onBackNavigation() },
                painter = painterResource(id = R.drawable.ic_arrow_left), contentDescription = null
            )
            Text(text = title, style = DTypography.t1)
            Spacer(Modifier.weight(1f))
            if (showSearchIcon) {
                Icon(
                    modifier = Modifier.noRippleClickable { onSearch() },
                    painter = painterResource(id = R.drawable.ic_search), contentDescription = null
                )
            }
        }
    }
}
