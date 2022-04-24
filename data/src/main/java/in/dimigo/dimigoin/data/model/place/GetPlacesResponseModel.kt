package `in`.dimigo.dimigoin.data.model.place

import `in`.dimigo.dimigoin.domain.entity.place.BuildingType
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType

data class GetPlacesResponseModel(
    val places: List<PlaceResponseModel>,
)

data class PlaceResponseModel(
    val _id: String,
    val type: PlaceType,
    val name: String,
    val nick: String?,
    val building: BuildingType,
    val floor: Int,
)
