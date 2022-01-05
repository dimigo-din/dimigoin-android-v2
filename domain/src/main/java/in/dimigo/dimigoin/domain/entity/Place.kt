package `in`.dimigo.dimigoin.domain.entity

data class Place(
    val _id: String,
    val name: String,
    val building: String,
    val floor: String?,
    val type: String,
) : PlaceSelectorDisplayable
