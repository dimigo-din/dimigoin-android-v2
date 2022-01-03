package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.domain.entity.Place

fun PlaceResponseModel.toEntity(): Place {
    val building = when {
        location.contains("본관") -> "본관"
        location.contains("신관") -> "신관"
        location.contains("학봉관") -> "학봉관"
        location.contains("우정학사") -> "우정학사"
        else -> "그 외"
    }

    val floor = when {
        location.contains("1층") -> "1층"
        location.contains("2층") -> "2층"
        location.contains("3층") -> "3층"
        location.contains("4층") -> "4층"
        else -> null
    }

    val type = when {
        name.contains("학년") || name == "과학실" -> "교실"
        name.contains("교무실") -> "교무실"
        name.contains("복도") -> "복도"
        name.contains("동아리") -> "동아리실"
        name.contains("급식실") -> "급식실"
        name.contains("옥상") -> "옥상"
        name.contains("골프장") -> "골프장"
        else -> "특별실"
    }

    return Place(_id, name, building, floor, type)
}