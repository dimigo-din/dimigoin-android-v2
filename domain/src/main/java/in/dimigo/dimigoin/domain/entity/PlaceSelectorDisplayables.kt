package `in`.dimigo.dimigoin.domain.entity

interface PlaceSelectorDisplayable

data class PlaceCategory(
    val name: String,
    val description: String,
) : PlaceSelectorDisplayable

data class Place(
    val _id: String,
    val name: String,
    val alias: String,
    val description: String,
    val building: String,
    val floor: String?,
    val type: PlaceType,
) : PlaceSelectorDisplayable

enum class PlaceType(val value: String) {
    CLASSROOM("교실"),
    RESTROOM("화장실"),
    CIRCLE("동아리실"),
    AFTERSCHOOL("방과후실"),
    TEACHER("교무실"),
    CORRIDOR("복도"),
    ETC("기타"),
    FARM("스마트팜"),
    PLAYGROUND("운동장"),
    GYM("체육관"),
    LAUNDRY("세탁"),
    ABSENT("결석"),
    SCHOOL("학교 건물"),
    DORMITORY("기숙사 건물")
}
