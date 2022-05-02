package `in`.dimigo.dimigoin.data.repository.fake

import `in`.dimigo.dimigoin.domain.entity.place.*
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class FakePlaceRepository : PlaceRepository {

    private var currentPlace: Place? = null
    private val favoriteAttendanceLogs = mutableListOf<AttendanceLog>()

    override suspend fun getPlaces(): Result<List<Place>> = Result.success(PLACES)

    override suspend fun setCurrentPlace(placeId: String, remark: String?): Result<Boolean> {
        PLACES.find { it._id == placeId }?.let {
            currentPlace = it
            return Result.success(true)
        }

        return Result.success(false)
    }

    override suspend fun getCurrentPlace(): Result<Place?> = Result.success(currentPlace)

    override suspend fun addFavoriteAttendanceLog(attendanceLog: AttendanceLog): Result<Boolean> {
        favoriteAttendanceLogs.add(attendanceLog)

        return Result.success(true)
    }

    override suspend fun removeFavoriteAttendanceLogById(id: String): Result<Boolean> {
        val result = favoriteAttendanceLogs.removeIf { it._id == id }

        return Result.success(result)
    }

    override suspend fun getFavoriteAttendanceLogs(): Result<List<AttendanceLog>> {
        return Result.success(favoriteAttendanceLogs)
    }

    override suspend fun getBuildings(): Result<List<Building>> {
        return Result.success(BUILDINGS)
    }

    companion object {
        // region PLACES, BUILDINGS
        val PLACES = listOf(
            Place(
                "1234",
                "1학년 5반",
                "1-5",
                "교실",
                "본관",
                "2층",
                PlaceType.CLASSROOM
            ),
            Place(
                "2345",
                "2학년 3반",
                "2-3",
                "교실",
                "본관",
                "2층",
                PlaceType.CLASSROOM
            ),
            Place(
                "3456",
                "열람실/도서관",
                "열람실",
                "특별실",
                "신관",
                "3층",
                PlaceType.ETC
            ),
            Place(
                "620fcff7a0469d4750585a34",
                "학봉관 호실",
                "남호실",
                "생활관",
                "학봉관",
                null,
                PlaceType.DORMITORY,
            ),
            Place(
                "620fcf9ca0469d4750585a32",
                "미술실",
                "미술실",
                "1층",
                "학봉관",
                "1층",
                PlaceType.ETC,
            ),
            Place(
                "620fcfc2a0469d4750585a33",
                "ATM기",
                "ATM",
                "1층",
                "학봉관",
                "1층",
                PlaceType.ETC,
            ),
            Place(
                "620fd105a0469d4750585a37",
                "우정학사 호실",
                "여호실",
                "생활관",
                "우정학사",
                null,
                PlaceType.DORMITORY,
            ),
            Place(
                "620fc9c3c1ce4101d43d5f98",
                "우정학사 세탁",
                "세탁",
                "세탁하러 오셨나요?",
                "우정학사",
                null,
                PlaceType.DORMITORY,
            ),
            Place(
                "620fcb32c1ce4101d43d5fa4",
                "스마트팜",
                "농장",
                "지상",
                "기타 장소",
                "지상",
                PlaceType.FARM,
            ),
            Place(
                "620fcb5ac1ce4101d43d5fa5",
                "운동장",
                "운동장",
                "지상",
                "기타 장소",
                "지상",
                PlaceType.PLAYGROUND,
            ),
            Place(
                "620fcb6ac1ce4101d43d5fa6",
                "체육관",
                "체육관",
                "지하",
                "기타 장소",
                "지하",
                PlaceType.GYM,
            ),
        )
        val BUILDINGS = listOf(
            Building(
                "학교", "본관",
                listOf(
                    PlaceCategory("B1층", "급식실"),
                    PlaceCategory("1층", "교무실, 교실"),
                    PlaceCategory("2층", "교실, 특별실"),
                    PlaceCategory("3층", "동아리실, 방과후실"),
                )
            ),
            Building(
                "학교", "신관",
                listOf(
                    PlaceCategory("1층", "교무실, 특별실"),
                    PlaceCategory("2층", "교실, 특별실"),
                    PlaceCategory("3층", "열람실, 특별실"),
                    PlaceCategory("4층", "대강당"),
                )
            ),
            Building(
                "생활관", "학봉관",
                listOf(
                    Place(
                        "620fcff7a0469d4750585a34",
                        "학봉관 호실",
                        "남호실",
                        "생활관",
                        "학봉관",
                        null,
                        PlaceType.DORMITORY,
                    ),
                    Place(
                        "620fcf9ca0469d4750585a32",
                        "미술실",
                        "미술실",
                        "1층",
                        "학봉관",
                        "1층",
                        PlaceType.ETC,
                    ),
                    Place(
                        "620fcfc2a0469d4750585a33",
                        "ATM기",
                        "ATM",
                        "1층",
                        "학봉관",
                        "1층",
                        PlaceType.ETC,
                    )
                ),
                listOf(
                    Place(
                        "620fcf4ea0469d4750585a31",
                        "학봉관 세탁",
                        "세탁",
                        "세탁하러 오셨나요?",
                        "학봉관",
                        null,
                        PlaceType.LAUNDRY,
                    )
                )
            ),
            Building(
                "생활관", "우정학사",
                listOf(
                    Place(
                        "620fd105a0469d4750585a37",
                        "우정학사 호실",
                        "여호실",
                        "생활관",
                        "우정학사",
                        null,
                        PlaceType.DORMITORY,
                    )
                ),
                listOf(
                    Place(
                        "620fc9c3c1ce4101d43d5f98",
                        "우정학사 세탁",
                        "세탁",
                        "세탁하러 오셨나요?",
                        "우정학사",
                        null,
                        PlaceType.DORMITORY,
                    )
                )
            ),
            Building(
                "기타", "기타 장소",
                listOf(
                    Place(
                        "620fcb32c1ce4101d43d5fa4",
                        "스마트팜",
                        "농장",
                        "지상",
                        "기타 장소",
                        "지상",
                        PlaceType.FARM,
                    ),
                    Place(
                        "620fcb5ac1ce4101d43d5fa5",
                        "운동장",
                        "운동장",
                        "지상",
                        "기타 장소",
                        "지상",
                        PlaceType.PLAYGROUND,
                    ),
                    Place(
                        "620fcb6ac1ce4101d43d5fa6",
                        "체육관",
                        "체육관",
                        "지하",
                        "기타 장소",
                        "지하",
                        PlaceType.GYM,
                    ),
                ),
                listOf(PlaceCategory("기타 장소 및 사유", "저는 다른 곳에 있어요"))
            ),
        )
        // endregion
    }
}