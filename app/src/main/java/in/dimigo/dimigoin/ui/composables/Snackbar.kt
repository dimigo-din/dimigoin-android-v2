package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.delay
import java.util.*

interface CustomSnackbarData {
    val id: Long
    val painter: Painter
    val message: AnnotatedString
    val description: String?
    val duration: Long
    var isVisible: MutableState<Boolean>
}

class CustomSnackbarHostState {
    val snackbarItems = mutableStateListOf<CustomSnackbarData>()

    suspend fun showSnackbar(
        painter: Painter,
        message: AnnotatedString,
        description: String? = null,
        duration: Long = 3_000L,
    ) {
        val currentSnackbar =
            CustomSnackbarDataImpl(painter = painter, message = message, description = description, duration = duration)
        snackbarItems.add(currentSnackbar)
        delay(50L)
        currentSnackbar.isVisible.value = true
        delay(duration)
        currentSnackbar.isVisible.value = false
        delay(1000L)
        snackbarItems.remove(currentSnackbar)
    }

    private class CustomSnackbarDataImpl(
        override val id: Long = UUID.randomUUID().mostSignificantBits,
        override val painter: Painter,
        override val message: AnnotatedString,
        override val description: String?,
        override val duration: Long,
    ) : CustomSnackbarData {
        override var isVisible: MutableState<Boolean> = mutableStateOf(false)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomSnackbarHost(
    hostState: CustomSnackbarHostState,
) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.TopCenter) {
        hostState.snackbarItems.forEach {
            key(it.id) {
                AnimatedVisibility(
                    visible = it.isVisible.value,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                ) {
                    Column {
                        Spacer(Modifier.statusBarsHeight())
                        CustomSnackbar(it)
                    }
                }
            }
        }
    }
}

@Composable
fun CustomSnackbar(snackbarData: CustomSnackbarData) {
    Card(
        modifier = Modifier
            .padding(20.dp)
            .width(310.dp)
            .requiredHeight(50.dp),
        shape = RoundedCornerShape(25.dp),
        elevation = 7.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = snackbarData.painter,
                contentDescription = null,
                tint = Point,
                modifier = Modifier.size(24.dp),
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = snackbarData.message,
                    textAlign = TextAlign.Center,
                    style = DTheme.typography.t5,
                )
                snackbarData.description?.let {
                    Text(
                        modifier = Modifier.padding(top = 1.dp),
                        text = it,
                        textAlign = TextAlign.Center,
                        style = DTheme.typography.t6,
                    )
                }
            }
        }
    }
}
