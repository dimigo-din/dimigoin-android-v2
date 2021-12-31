package `in`.dimigo.dimigoin.ui.composables

import `in`.dimigo.dimigoin.ui.theme.MagentaDimigo
import `in`.dimigo.dimigoin.ui.theme.Typography
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarData
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.UUID
import kotlinx.coroutines.delay

interface CustomSnackbarData {
    val id: Long
    val painter: Painter
    val message: AnnotatedString
    val duration: Long
    var isVisible: MutableState<Boolean>
}


class CustomSnackbarHostState {
    val snackbarItems = mutableStateListOf<CustomSnackbarData>()

    suspend fun showSnackbar(
        painter: Painter,
        message: AnnotatedString,
        duration: Long = 5000L,
    ) {
        val currentSnackbar = CustomSnackbarDataImpl(painter = painter, message = message, duration = duration)
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
                LaunchedEffect(Unit) {
                    it.isVisible.value = true
                    delay(it.duration)
                    it.isVisible.value = false
                }
                AnimatedVisibility(
                    visible = it.isVisible.value,
                    enter = slideInVertically() + fadeIn(),
                    exit = slideOutVertically() + fadeOut(),
                ) {
                    CustomSnackbar(it)
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
            .height(50.dp),
        shape = RoundedCornerShape(25.dp),
        elevation = 7.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 13.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = snackbarData.painter,
                contentDescription = null,
                tint = MagentaDimigo,
                modifier = Modifier.size(24.dp),
            )
            Text(
                text = snackbarData.message,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textAlign = TextAlign.Center,
                style = Typography.subtitle1,
            )
        }
    }
}
