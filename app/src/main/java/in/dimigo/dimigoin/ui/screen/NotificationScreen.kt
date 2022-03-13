package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import `in`.dimigo.dimigoin.ui.theme.C4
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun NotificationScreen(
    onBackNavigation: () -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Spacer(Modifier.statusBarsHeight())
            PlaceSelectorTopBar(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 26.dp),
                title = "알림",
                onBackNavigation = onBackNavigation,
                showSearchIcon = false,
                onSearch = { },
                color = MaterialTheme.colors.onSurface
            )
            Divider(color = C4)
        }
    }
}
