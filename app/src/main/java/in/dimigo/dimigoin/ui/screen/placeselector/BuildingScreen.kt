package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.entity.PlaceCategory
import `in`.dimigo.dimigoin.ui.composables.BuildingList
import `in`.dimigo.dimigoin.ui.composables.ContentBox
import `in`.dimigo.dimigoin.ui.composables.PlaceCategoryItem
import `in`.dimigo.dimigoin.ui.composables.PlaceItem
import `in`.dimigo.dimigoin.ui.composables.PlaceSelectorTopBar
import `in`.dimigo.dimigoin.ui.theme.C2
import `in`.dimigo.dimigoin.ui.theme.DTypography
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.viewmodel.PlaceSelectorViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.getViewModel

@Composable
fun BuildingScreen(
    modifier: Modifier = Modifier,
    placeSelectorViewModel: PlaceSelectorViewModel = getViewModel(),
    title: String,
    onBackNavigation: () -> Unit,
    onSearch: () -> Unit,
    onBuildingClick: (Building) -> Unit,
    onCategoryClick: (PlaceCategory) -> Unit,
    onPlaceChange: (Place, String?) -> Unit,
    onFavoriteAdd: (Place, String?) -> Unit,
    onFavoriteRemove: (Place) -> Unit,
) {
    val currentPlace = placeSelectorViewModel.currentPlace.collectAsState().value
    val buildings = placeSelectorViewModel.recommendedBuildings.collectAsState().value
    val favorites = placeSelectorViewModel.favoriteAttendanceLog.collectAsState().value

    Column(
        modifier = Modifier
            .padding(top = 36.dp),
    ) {
        PlaceSelectorTopBar(
            modifier = Modifier.padding(horizontal = 20.dp),
            title = "위치 선택",
            onBackNavigation = onBackNavigation,
            showSearchIcon = true,
            onSearch = onSearch,
            color = C2,
        )
        Spacer(Modifier.height(26.dp))

        LazyColumn {
            item {
                when (buildings) {
                    is Future.Success -> BuildingList(
                        modifier = Modifier.fillMaxWidth(),
                        buildings = buildings._data,
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
                            favorites.data?.let {
                                if (it.isEmpty()) Text(text = "등록된 즐겨찾기가 없습니다", style = DTypography.explainText)

                                it.forEach { attLog ->
                                    val isSelected = currentPlace.data?._id == attLog.placeId
                                    val place = placeSelectorViewModel.placeIdToPlace(attLog.placeId)
                                    PlaceItem(
                                        place = place.copy(type = attLog.remark ?: "사유가 등록되지 않음"),
                                        icon = R.drawable.ic_main,
                                        isFavorite = true,
                                        onFavoriteChange = {
                                            placeSelectorViewModel.removeFavoriteAttendanceLog(attLog,
                                                onFavoriteRemove)
                                        },
                                        isSelected = isSelected,
                                        onSelect = {
                                            if (!isSelected) placeSelectorViewModel.setCurrentPlace(
                                                place,
                                                attLog.remark,
                                                onPlaceChange
                                            )
                                        }
                                    )
                                }
                            }
                        } else {
                            val building = buildings.data?.find { it.name == title }
                            building?.children?.forEach { displayable ->
                                when (displayable) {
                                    is Place -> {
                                        val favoriteAttLog = favorites.data?.find { it.placeId == displayable._id }
                                        val isFavorite = favoriteAttLog != null
                                        val isSelected = displayable._id == currentPlace.data?._id
                                        PlaceItem(
                                            place = displayable,
                                            icon = R.drawable.ic_school,
                                            isFavorite = isFavorite,
                                            onFavoriteChange = onFavoriteChange@{ favorite ->
                                                if (favorite) {
                                                    placeSelectorViewModel.addFavoriteAttendanceLog(
                                                        displayable,
                                                        "테스트 사유",
                                                        onFavoriteAdd,
                                                    )
                                                } else {
                                                    placeSelectorViewModel.removeFavoriteAttendanceLog(
                                                        favorites.data?.find { it.placeId == displayable._id }
                                                            ?: return@onFavoriteChange,
                                                        onFavoriteRemove
                                                    )
                                                }
                                            },
                                            isSelected = isSelected,
                                            onSelect = {
                                                if (!isSelected) placeSelectorViewModel.setCurrentPlace(
                                                    displayable,
                                                    "테스트 사유",
                                                    onPlaceChange
                                                )
                                            }
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
                        }
                    }
                }

                Spacer(Modifier.height(20.dp))
            }
        }
    }
}
