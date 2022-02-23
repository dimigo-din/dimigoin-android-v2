package `in`.dimigo.dimigoin.domain.entity.place

data class Building(
    val type: String,
    val name: String,
    val children: List<PlaceSelectorDisplayable>,
    val extraChildren: List<PlaceSelectorDisplayable>? = null,
)
