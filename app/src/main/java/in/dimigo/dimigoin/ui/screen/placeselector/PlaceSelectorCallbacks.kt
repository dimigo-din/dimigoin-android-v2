package `in`.dimigo.dimigoin.ui.screen.placeselector

import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory

interface PlaceSelectorCallbacks {
    val onCategoryClick: (PlaceCategory) -> Unit

    val onTryPlaceChange: (Place) -> Unit
    val onPlaceChange: (Place, String?) -> Unit

    val onTryFavoriteAdd: (Place) -> Unit
    val onFavoriteRemove: (Place) -> Unit
}