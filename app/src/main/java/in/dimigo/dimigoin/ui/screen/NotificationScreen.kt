package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.insets.statusBarsHeight

@Composable
fun NotificationScreen(
    onBackNavigation: () -> Unit,
) {
    Surface(Modifier.fillMaxSize()) {
        Column {
            Spacer(Modifier.statusBarsHeight())
            PlaceSelectorTopBar(
                title = "알림",
                onBackNavigation = onBackNavigation,
                showSearchIcon = false,
                onSearch = { },
                color = MaterialTheme.colors.onSurface
            )
        }
    }
}
