package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.ui.composables.PlaceItem
import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import `in`.dimigo.dimigoin.ui.theme.C4
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
        val places = placeSelectorViewModel.getCategoryFilteredPlaces(title)
        val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value
        val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value

        Column {
            PlaceSelectorTopBar(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = title,
                onBackNavigation = onBackNavigation,
                showSearchIcon = false,
                onSearch = { },
                color = MaterialTheme.colors.onSurface,
            )
            Spacer(Modifier.height(26.dp))
            Divider(color = C4)
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
