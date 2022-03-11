package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.domain.entity.place.Place

fun PlaceResponseModel.toEntity(): Place {
    val building = when (this.building) {
        "MAIN" -> "본관"
        "NEWBUILDING" -> "신관"
        "HAKBONG" -> "학봉관"
        "UJEONG" -> "우정학사"
        else -> "기타 장소"
    }

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
        1, 2, 3, 4 -> "${this.floor}층"
        -1 -> "B1층"
        else -> when (this.name) {
            "결석", "외출", "위치 불명" -> "기타 장소 및 사유"
            else -> null
        }
    }

    return Place(
        _id = _id,
        name = name,
        alias = nick ?: name,
        building = building,
        description = description,
        floor = floor,
        type = type,
    )
}