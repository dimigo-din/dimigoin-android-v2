package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun BottomNavigation(

) {
    androidx.compose.material.BottomNavigation() {
        
    }
}

@Composable
fun BottomNavigationItem(
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
) = Column(
    modifier = Modifier.noRippleClickable { onClick() },
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(7.dp),
) {
    val color = if (selected) Point else C3

    CompositionLocalProvider(LocalContentColor provides color) {
        icon()
        label()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Bottom Navigation Bar Item (not selected)")
@Composable
fun BNItemPreview() {
    BottomNavigationItem(
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        label = { Text("Favorite", style = DTypography.t6) },
        selected = false,
        onClick = { },
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, name = "Bottom Navigation Bar Item (not selected)")
@Composable
fun BNItemPreviewSelected() {
    BottomNavigationItem(
        icon = { Icon(Icons.Filled.Favorite, contentDescription = null) },
        label = { Text("Favorite", style = DTypography.t6) },
        selected = true,
        onClick = { },
    )
}
