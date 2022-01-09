package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.ui.composables.PlaceItem
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.C3
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.getViewModel

@Composable
fun PlaceSearchScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    onBackNavigation: () -> Unit,
    onTryPlaceChange: (Place) -> Unit,
    onTryFavoriteAdd: (Place) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
    color: Color,
) = Box(
    modifier = modifier
) {
    Surface(
        Modifier.padding(top = 26.dp)
    ) {
        val (search, setSearch) = remember { mutableStateOf("") }
        val places = placeSelectorViewModel.getFilteredPlaceByName(search)
        val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value
        val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value
        Column {
            Row(
                Modifier.padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CompositionLocalProvider(LocalContentColor provides color) {
                    Icon(
                        modifier = Modifier
                            .padding(end = 30.dp)
                            .noRippleClickable { onBackNavigation() },
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = null
                    )
                    CustomTextField(
                        modifier = Modifier,
                        value = search,
                        onValueChange = setSearch,
                        placeholder = {
                            Text("실 이름, 고유번호 검색",
                                style = DTypography.t3.copy(fontWeight = FontWeight.W500),
                                color = C3
                            )
                        }
                    )
                }
            }
            Spacer(Modifier.height(26.dp))
            Divider(color = C4)
            if (search.isBlank()) {
                Column(
                    modifier = Modifier
                        .align(alignment = CenterHorizontally)
                        .padding(top = 60.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                ) {
                    Text(
                        modifier = Modifier.align(alignment = CenterHorizontally),
                        text = "검색어를 입력해주세요",
                        style = DTypography.t3,
                        color = C2,
                        textAlign = TextAlign.Center
                    )
                    Text("실 이름을 직접 입력하거나,\n" +
                            "각 실에 붙어있는 고유번호로 찾을수도 있어요",
                        style = DTypography.pageSubtitle.copy(lineHeight = 22.sp),
                        color = C2,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(40.dp),
                    modifier = Modifier.padding(horizontal = 20.dp)
                ) {
                    item { Spacer(Modifier) }
                    items(places) { place ->
                        val favoriteAttLog = favorites.data?.find { it.placeId == place._id }
                        val isFavorite = favoriteAttLog != null
                        val isSelected = place._id == currentPlace.data?._id
                        PlaceItem(
                            place = place,
                            icon = R.drawable.ic_school,
                            isFavorite = isFavorite,
                            onFavoriteChange = onFavoriteChange@{ favorite ->
                                if (favorite) {
                                    onTryFavoriteAdd(place)
                                } else {
                                    placeSelectorViewModel.removeFavoriteAttendanceLog(
                                        favorites.data?.find { it.placeId == place._id }
                                            ?: return@onFavoriteChange,
                                        onFavoriteRemove
                                    )
                                }
                            },
                            isSelected = isSelected,
                            onSelect = { onTryPlaceChange(place) },
                        )
                    }
                    item { Spacer(Modifier) }
                }
            }
        }
    }
}

@Composable
private fun CustomTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: @Composable () -> Unit,
) {
    var focused by remember { mutableStateOf(false) }
    BasicTextField(
        value = value, onValueChange = onValueChange,
        textStyle = DTypography.t3.copy(
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.onSurface,
        ),
        cursorBrush = SolidColor(Point),
        modifier = modifier.onFocusChanged {
            focused = it.isFocused
        },
    ) { innerTextField ->
        if (value.isEmpty()) {
            placeholder()
        }
        innerTextField()
    }
}
