package `in`.dimigo.dimigoin.data.model.place

data class AllPlaceResponseModel(
    val places: List<PlaceResponseModel>,
)

data class PlaceResponseModel(
    val _id: String,
    val type: String,
    val name: String,
    val location: String,
)
