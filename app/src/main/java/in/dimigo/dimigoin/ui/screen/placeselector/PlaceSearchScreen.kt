package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.ui.composables.PlaceItem
import `in`.dimigo.dimigoin.ui.composables.modifiers.noRippleClickable
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.insets.LocalWindowInsets
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
    val insets = LocalWindowInsets.current
    val imeBottom = with(LocalDensity.current) { insets.ime.bottom.toDp() }
    Surface(
        Modifier.padding(top = 26.dp, bottom = imeBottom)
    ) {
        val (search, setSearch) = remember { mutableStateOf("") }
        val places = placeSelectorViewModel.getFilteredPlaceByName(search)
        val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value.asOptional()
        val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value.asOptional()
        Column {
            Spacer(Modifier.height(4.dp))
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
                                style = DTheme.typography.t3.copy(fontWeight = FontWeight.W500),
                                color = DTheme.colors.c3
                            )
                        }
                    )
                }
            }
            Spacer(Modifier.height(26.dp))
            Divider(color = DTheme.colors.c4)
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
                        style = DTheme.typography.t3,
                        color = DTheme.colors.c2,
                        textAlign = TextAlign.Center
                    )
                    Text("실 이름을 직접 입력하거나,\n" +
                        "각 실에 붙어있는 고유번호로 찾을수도 있어요",
                        style = DTheme.typography.pageSubtitle.copy(lineHeight = 22.sp),
                        color = DTheme.colors.c2,
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
                        val favoriteAttLog = favorites.map { it.find { it.placeId == place._id } }.orElse(null)
                        val isFavorite = favoriteAttLog != null
                        val isSelected = place._id == currentPlace.map { it._id }.orElse(null)
                        PlaceItem(
                            place = place,
                            isFavorite = isFavorite,
                            onFavoriteChange = onFavoriteChange@{ favorite ->
                                if (favorite) {
                                    onTryFavoriteAdd(place)
                                } else {
                                    placeSelectorViewModel.removeFavoriteAttendanceLog(
                                        favorites.map { it.find { it.placeId == place._id } }.orElse(null)
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
        textStyle = DTheme.typography.t3.copy(
            fontWeight = FontWeight.W500,
            textAlign = TextAlign.Start,
            color = MaterialTheme.colors.onSurface,
        ),
        singleLine = true,
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
