package `in`.dimigo.dimigoin.ui.screen

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.ui.composables.ContentBox
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.MainViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = getViewModel(),
    onPlaceSelectorNavigate: () -> Unit,
    onPlaceSelect: (Place) -> Unit,
    hasNewNotification: Boolean,
) = Column(modifier) {
    val currentPlace = mainViewModel.currentPlace.collectAsState().value

    Row(Modifier.fillMaxWidth()) {
        val notificationIcon = if (hasNewNotification) {
            R.drawable.ic_notification_true
        } else {
            R.drawable.ic_notification_false
        }
        Spacer(Modifier.width(10.dp))
        Icon(painter = painterResource(id = R.drawable.ic_dimigoin), contentDescription = null, tint = Point)
        Spacer(Modifier.weight(1f))
        Icon(painter = painterResource(id = notificationIcon), contentDescription = null)
    }

    Spacer(Modifier.height(26.dp))

    ContentBox(
        title = "나의 위치",
        summary = buildAnnotatedString {
            when (currentPlace) {
                is Future.Success -> {
                    append("나의 위치는 현재 ")
                    withStyle(SpanStyle(color = Point)) { append(currentPlace._data.name) }
                    append("입니다")
                }
                is Future.Failure -> append("위치 정보를 불러오지 못했습니다")
                is Future.Loading, is Future.Nothing -> append("위치 정보를 가져오는 중입니다")
            }
        },
        onNavigate = onPlaceSelectorNavigate,
    ) {

    }
}
