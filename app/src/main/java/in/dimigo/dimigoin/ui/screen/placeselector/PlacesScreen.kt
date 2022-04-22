package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.ui.composables.PlaceItem
import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    title: String,
    onBackNavigation: () -> Unit,
    onTryPlaceChange: (Place) -> Unit,
    onTryFavoriteAdd: (Place) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) = Box(
    modifier = modifier
) {
    Surface(
        Modifier.padding(top = 26.dp)
    ) {
        val places = placeSelectorViewModel.getFilteredPlaceByCategory(title)
        val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value.asOptional()
        val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value.asOptional()

        Column {
            PlaceSelectorTopBar(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = if (title == "기타 장소 기타 장소 및 사유") "기타 장소 및 사유" else title,
                onBackNavigation = onBackNavigation,
                showSearchIcon = false,
                onSearch = { },
                color = MaterialTheme.colors.onSurface,
            )
            Spacer(Modifier.height(26.dp))
            Divider(color = DTheme.colors.c4)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(40.dp),
            ) {
                item { Spacer(Modifier) }
                items(places.sortedBy { it.name }) { place ->
                    val favoriteAttLog = favorites.map { it.find { it.placeId == place._id } }.orElse(null)
                    val isFavorite = favoriteAttLog != null
                    val isSelected = place._id == currentPlace.map { it._id }.orElse(null)
                    PlaceItem(
                        modifier = Modifier.padding(horizontal = 20.dp),
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
