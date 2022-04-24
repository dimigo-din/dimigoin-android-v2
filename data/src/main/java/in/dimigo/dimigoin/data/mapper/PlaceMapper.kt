package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.data.util.gson
import `in`.dimigo.dimigoin.domain.entity.place.Floor
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory

fun String.toPlace(): Place {
    return gson.fromJson(this, Place::class.java)
}

fun Place.toJsonString(): String {
    return gson.toJson(this)
}

fun PlaceResponseModel.toEntity(): Place {
    val description = when (this.name) {
        "미술실", "ATM기" -> "1층"
        "학봉관 호실", "우정학사 호실" -> "생활관"
        "학봉관 세탁", "우정학사 세탁" -> "세탁하러 오셨나요?"
        "스마트팜", "운동장" -> "지상"
        "체육관" -> "지하"
        "결석" -> "학교를 빠졌어요"
        "외출" -> "밖에 나갔어요"
        "위치 불명" -> "여기가 어딜까요..."
        else -> type.value
    }

    val floor = when (this.floor) {
        1, 2, 3, 4 -> Floor.of(this.floor)
        -1 -> Floor.of(0)
        else -> Floor.none()
    }

    val placeCategory = if (this.name in etcCategoryPlaces) {
        PlaceCategory.ETC
    } else {
        PlaceCategory.from(building, floor)
    }

    return Place(
        _id = _id,
        name = name,
        alias = nick ?: name,
        description = description,
        placeCategory = placeCategory,
        type = type,
    )
}

private val etcCategoryPlaces = listOf("결석", "외출", "위치 불명")