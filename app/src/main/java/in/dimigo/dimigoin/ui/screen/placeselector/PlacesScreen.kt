package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    category: PlaceCategory,
    onBackNavigation: () -> Unit,
    callbacks: PlaceSelectorCallbacks,
) = Box(
    modifier = modifier
) {
    Surface(
        Modifier.padding(top = 26.dp)
    ) {
        val places by remember {
            mutableStateOf(placeSelectorViewModel.getFilteredPlaceByCategory(category))
        }
        val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value.asOptional()
        val favorites =
            placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value.asOptional()

        Column {
            PlaceSelectorTopBar(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = when (category) {
                    is PlaceCategory.FloorCategory -> category.building.value + " " + category.floor.toString()
                    is PlaceCategory.NamedCategory -> category.name
                    PlaceCategory.None -> ""
                },
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
                    val favoriteAttLog =
                        favorites.map { it.find { it.placeId == place._id } }.orElse(null)
                    val isFavorite = favoriteAttLog != null
                    val isSelected = place._id == currentPlace.map { it._id }.orElse(null)
                    PlaceItem(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        place = place,
                        isFavorite = isFavorite,
                        onFavoriteChange = onFavoriteChange@{ favorite ->
                            if (favorite) {
                                callbacks.onTryFavoriteAdd(place)
                            } else {
                                placeSelectorViewModel.removeFavoriteAttendanceLog(
                                    favorites.map { it.find { it.placeId == place._id } }
                                        .orElse(null)
                                        ?: return@onFavoriteChange,
                                    callbacks.onFavoriteRemove
                                )
                            }
                        },
                        isSelected = isSelected,
                        onSelect = { callbacks.onTryPlaceChange(place) },
                    )
                }
                item { Spacer(Modifier) }
            }
        }
    }
}
