package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.*
import `in`.dimigo.dimigoin.ui.composables.*
import `in`.dimigo.dimigoin.ui.theme.DTheme
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel
import java.util.*

@Composable
fun BuildingScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    title: String,
    onBackNavigation: () -> Unit,
    onSearch: () -> Unit,
    onBuildingClick: (Building) -> Unit,
    onCategoryClick: (PlaceCategory) -> Unit,
    onTryPlaceChange: (Place) -> Unit,
    onPlaceChange: (Place, String?) -> Unit,
    onTryFavoriteAdd: (Place) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) {
    val context = LocalContext.current
    val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value.asOptional()
    val buildings = placeSelectorViewModel.recommendedBuildings.collectAsState().value
    val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value
    val building = buildings.asOptional()
        .map { it.find { it.name == title } }

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

        LazyColumn {
            item {
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
                    title = title
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(25.dp)) {
                        if (title == "즐겨찾기") {
                            if (favorites is Future.Success) {
                                favorites.data.let {
                                    if (it.isEmpty()) Text(
                                        text = "등록된 즐겨찾기가 없습니다",
                                        style = DTheme.typography.explainText,
                                        color = DTheme.colors.c2,
                                    )

                                    it.forEach { attLog ->
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
                                                    onFavoriteRemove
                                                )
                                            },
                                            isSelected = isSelected,
                                            onSelect = {
                                                if (!isSelected) placeSelectorViewModel.setCurrentPlace(
                                                    place,
                                                    attLog.remark,
                                                    onPlaceChange,
                                                    false,
                                                    context
                                                )
                                            }
                                        )
                                    }
                                }
                            }
                        } else {
                            building.map { it?.children }.orElse(emptyList())
                                ?.forEach { displayable ->
                                    PlaceSelectorDisplayableItem(
                                        placeSelectorViewModel = placeSelectorViewModel,
                                        displayable = displayable,
                                        favorites = favorites.asOptional(),
                                        currentPlace = currentPlace,
                                        onCategoryClick = onCategoryClick,
                                        onTryPlaceChange = onTryPlaceChange,
                                        onTryFavoriteAdd = onTryFavoriteAdd,
                                        onFavoriteRemove = onFavoriteRemove
                                    )
                                }
                        }
                    }
                }

                Spacer(Modifier.height(11.dp))

                val extraChildren =
                    building.map { it?.extraChildren ?: emptyList() }.orElse(emptyList())
                if (extraChildren.isNotEmpty()) {
                    ContentBox(
                        modifier = Modifier.padding(horizontal = 20.dp),
                    ) {
                        extraChildren.forEach { displayable ->
                            PlaceSelectorDisplayableItem(
                                placeSelectorViewModel = placeSelectorViewModel,
                                displayable = displayable,
                                favorites = favorites.asOptional(),
                                currentPlace = currentPlace,
                                onCategoryClick = onCategoryClick,
                                onTryPlaceChange = onTryPlaceChange,
                                onTryFavoriteAdd = onTryFavoriteAdd,
                                onFavoriteRemove = onFavoriteRemove
                            )
                        }
                    }
                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

@Composable
fun PlaceSelectorDisplayableItem(
    placeSelectorViewModel: PlaceSelectorViewModel,
    displayable: PlaceSelectorDisplayable,
    favorites: Optional<List<AttendanceLog>>,
    currentPlace: Optional<Place>,
    onCategoryClick: (PlaceCategory) -> Unit,
    onTryPlaceChange: (Place) -> Unit,
    onTryFavoriteAdd: (Place) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) {
    when (displayable) {
        is Place -> {
            val favoriteAttLog = favorites.map { it.find { it.placeId == displayable._id } }
            val isFavorite = favoriteAttLog != null
            val isSelected = displayable._id == currentPlace.map { it._id }.orElse(null)
            PlaceItem(
                place = displayable,
                isFavorite = isFavorite,
                onFavoriteChange = onFavoriteChange@{ favorite ->
                    if (favorite) {
                        onTryFavoriteAdd(displayable)
                    } else {
                        placeSelectorViewModel.removeFavoriteAttendanceLog(
                            favorites.map { it.find { it.placeId == displayable._id } }.orElse(null)
                                ?: return@onFavoriteChange,
                            onFavoriteRemove
                        )
                    }
                },
                isSelected = isSelected,
                onSelect = { onTryPlaceChange(displayable) }
            )
        }
        is PlaceCategory -> {
            PlaceCategoryItem(
                category = displayable,
                icon = R.drawable.ic_school,
                onClick = { onCategoryClick(displayable) }
            )
        }
    }
}