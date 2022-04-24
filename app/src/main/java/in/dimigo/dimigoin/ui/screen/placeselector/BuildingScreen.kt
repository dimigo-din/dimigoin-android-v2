package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.*
import `in`.dimigo.dimigoin.ui.composables.*
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import java.util.*

@Composable
fun BuildingScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    buildingDisplayable: BuildingDisplayable,
    onBackNavigation: () -> Unit,
    onSearch: () -> Unit,
    onBuildingClick: (Building) -> Unit,
    placeSelectorCallbacks: PlaceSelectorCallbacks,
    reasonCallbacks: PlaceSelectorViewModel.Callbacks,
) {
    val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value
    val buildings = placeSelectorViewModel.recommendedBuildings.collectAsState().value
    val favorites = when (
        val favoritesState = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value
    ) {
        is Future.Success -> favoritesState.data
        else -> emptyList()
    }

    Column(
        modifier = modifier
            .padding(top = 26.dp, bottom = 60.dp),
    ) {
        PlaceSelectorTopBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = "위치 선택",
            onBackNavigation = onBackNavigation,
            showSearchIcon = true,
            onSearch = onSearch,
            color = DTheme.colors.c2,
        )
        Spacer(Modifier.height(26.dp))

        Column(Modifier.verticalScroll(rememberScrollState())) {
            when (buildings) {
                is Future.Success -> BuildingList(
                    modifier = Modifier.fillMaxWidth(),
                    buildings = buildings.data,
                    onBuildingClick = onBuildingClick,
                )
                else -> Spacer(Modifier.height(160.dp))
            }
            Spacer(Modifier.height(25.dp))
            ContentBox(
                modifier = Modifier.padding(horizontal = 20.dp),
                title = buildingDisplayable.getName()
            ) {
                ChildrenContent(
                    buildingDisplayable,
                    placeSelectorViewModel,
                    favorites,
                    currentPlace.asOptional(),
                    placeSelectorCallbacks,
                    reasonCallbacks,
                )
            }

            Spacer(Modifier.height(11.dp))

            if (buildingDisplayable is Building.ValueWithExtra) {
                ContentBox(
                    modifier = Modifier.padding(horizontal = 20.dp),
                ) {
                    buildingDisplayable.extraChildren.forEach { displayable ->
                        PlaceDisplayableItem(
                            placeSelectorViewModel = placeSelectorViewModel,
                            displayable = displayable,
                            favorites = favorites,
                            currentPlace = currentPlace.asOptional(),
                            callbacks = placeSelectorCallbacks,
                        )
                    }
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
fun ChildrenContent(
    buildingDisplayable: BuildingDisplayable,
    placeSelectorViewModel: PlaceSelectorViewModel,
    favorites: List<AttendanceLog>,
    currentPlace: Optional<Place>,
    placeSelectorCallbacks: PlaceSelectorCallbacks,
    reasonCallbacks: PlaceSelectorViewModel.Callbacks,
) {
    Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
        when (buildingDisplayable) {
            FavoritePlaces -> FavoritePlaceColumn(
                placeSelectorViewModel,
                favorites,
                currentPlace,
                placeSelectorCallbacks,
                reasonCallbacks,
            )
            is Building.Default -> {
                buildingDisplayable.children.forEach { placeDisplayable ->
                    PlaceDisplayableItem(
                        placeSelectorViewModel = placeSelectorViewModel,
                        displayable = placeDisplayable,
                        favorites = favorites,
                        currentPlace = currentPlace,
                        callbacks = placeSelectorCallbacks,
                    )
                }
            }
        }
    }
}

@Composable
fun FavoritePlaceColumn(
    placeSelectorViewModel: PlaceSelectorViewModel,
    favorites: List<AttendanceLog>,
    currentPlace: Optional<Place>,
    placeSelectorCallbacks: PlaceSelectorCallbacks,
    reasonCallbacks: PlaceSelectorViewModel.Callbacks,
) {
    if (favorites.isEmpty()) Text(
        text = "등록된 즐겨찾기가 없습니다",
        style = DTheme.typography.explainText,
        color = DTheme.colors.c2,
    )

    favorites.forEach { attLog ->
        val isSelected = currentPlace.map { it._id }.orElse(null) == attLog.placeId
        val place =
            placeSelectorViewModel.placeIdToPlace(attLog.placeId)
        PlaceItem(
            place = place.copy(
                description = attLog.remark
                    ?: "사유가 등록되지 않음"
            ),
            isFavorite = true,
            onFavoriteChange = {
                placeSelectorViewModel.removeFavoriteAttendanceLog(
                    attLog,
                    placeSelectorCallbacks.onFavoriteRemove
                )
            },
            isSelected = isSelected,
            onSelect = {
                if (!isSelected) placeSelectorViewModel.setCurrentPlace(
                    place,
                    attLog.remark,
                    reasonCallbacks,
                    false,
                )
            }
        )
    }

}

@Composable
fun PlaceDisplayableItem(
    placeSelectorViewModel: PlaceSelectorViewModel,
    displayable: PlaceDisplayable,
    favorites: List<AttendanceLog>,
    currentPlace: Optional<Place>,
    callbacks: PlaceSelectorCallbacks,
) {
    when (displayable) {
        is Place -> {
            val favoriteAttLog = favorites.find { it.placeId == displayable._id }
            val isFavorite = favoriteAttLog != null
            val isSelected = displayable._id == currentPlace.map { it._id }.orElse(null)
            PlaceItem(
                place = displayable,
                isFavorite = isFavorite,
                onFavoriteChange = onFavoriteChange@{ favorite ->
                    if (favorite) {
                        callbacks.onTryFavoriteAdd(displayable)
                    } else {
                        placeSelectorViewModel.removeFavoriteAttendanceLog(
                            favorites.find { it.placeId == displayable._id }
                                ?: return@onFavoriteChange,
                            callbacks.onFavoriteRemove
                        )
                    }
                },
                isSelected = isSelected,
                onSelect = { callbacks.onTryPlaceChange(displayable) }
            )
        }
        is PlaceCategory -> {
            PlaceCategoryItem(
                category = displayable,
                icon = R.drawable.ic_school,
                onClick = { callbacks.onCategoryClick(displayable) }
            )
        }
    }
}