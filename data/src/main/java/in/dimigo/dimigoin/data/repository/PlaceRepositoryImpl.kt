package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceRequestModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.entity.PlaceCategory
import `in`.dimigo.dimigoin.domain.entity.PlaceType
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PlaceRepositoryImpl(
    private val service: DimigoinApiService,
    private val sharedPreferenceManager: LocalSharedPreferenceManager,
) : PlaceRepository {
    private var places: List<Place>? = null
    private var currentPlace: Place? = null

    val attendanceLogMutex = Mutex()

    override suspend fun getAllPlaces(): Result<List<Place>> = resultFromCall(
        service.getPlaces(),
        cached = places,
    ) { response ->
        response.places.map(PlaceResponseModel::toEntity).also {
            places = it
        }
    }

    override suspend fun setCurrentPlace(placeId: String, remark: String?): Result<Boolean> = resultFromCall(
        service.addAttendanceLog(PostAttendanceRequestModel(placeId, remark))
    ) { response ->
        places?.find { it._id == response.attendanceLog.placeId }.also {
            currentPlace = it
        } != null
    }

    override suspend fun getCurrentPlace(): Result<Place?> = resultFromCall(
        service.getAttendanceLogs()
    ) { response ->
        response.logs.firstOrNull()?.place?.toEntity().also {
            currentPlace = it
        }
    }

    override suspend fun addFavoriteAttendanceLog(attendanceLog: AttendanceLog): Result<Boolean> {
        return try {
            attendanceLogMutex.withLock {
                sharedPreferenceManager.favoriteAttendanceLogs += attendanceLog
            }
            attendanceLogMutex.withLock {
                Result.success(attendanceLog in sharedPreferenceManager.favoriteAttendanceLogs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun removeFavoriteAttendanceLog(id: String): Result<Boolean> {
        return try {
            attendanceLogMutex.withLock {
                val logs = sharedPreferenceManager.favoriteAttendanceLogs
                val logToRemove = logs.find { it._id == id } ?: return Result.success(false)
                sharedPreferenceManager.favoriteAttendanceLogs = logs - logToRemove
            }
            attendanceLogMutex.withLock {
                Result.success(sharedPreferenceManager.favoriteAttendanceLogs.find { it._id == id } == null)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteAttendanceLogs(): Result<List<AttendanceLog>> {
        return try {
            attendanceLogMutex.withLock {
                Result.success(sharedPreferenceManager.favoriteAttendanceLogs)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun getBuildings(): Result<List<Building>> {
        return Result.success(
            listOf(
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
                    listOf(Place(
                        "620fd105a0469d4750585a37",
                        "우정학사 호실",
                        "여호실",
                        "생활관",
                        "우정학사",
                        null,
                        PlaceType.DORMITORY,
                    )),
                    listOf(Place(
                        "620fc9c3c1ce4101d43d5f98",
                        "우정학사 세탁",
                        "세탁",
                        "세탁하러 오셨나요?",
                        "우정학사",
                        null,
                        PlaceType.DORMITORY,
                    ))
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
        )
    }
}
