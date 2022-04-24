package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceRequestModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.place.*
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PlaceRepositoryImpl(
    private val service: DimigoinApiService,
    private val sharedPreferenceManager: LocalSharedPreferenceManager,
) : PlaceRepository {
    private var places: List<Place>? = null
    private var currentPlace: Place? = null

    private val attendanceLogMutex = Mutex()
    private val placesMutex = Mutex()

    override suspend fun getPlaces(): Result<List<Place>> {
        return getPlacesFromLocal().recoverCatching {
            getPlacesFromRemote().getOrThrow()
        }
    }

    private suspend fun getPlacesFromRemote(): Result<List<Place>> = resultFromCall(
        service.getPlaces(),
        cached = places,
    ) { response ->
        response.places.map(PlaceResponseModel::toEntity).also {
            places = it
            sharedPreferenceManager.places = it
        }
    }

    private suspend fun getPlacesFromLocal(): Result<List<Place>> = placesMutex.withLock {
        sharedPreferenceManager.places?.let {
            places = it
            Result.success(it)
        } ?: Result.failure(NoSuchElementException("Places are not stored in local"))
    }

    override suspend fun setCurrentPlace(placeId: String, remark: String?): Result<Boolean> =
        resultFromCall(
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

    override suspend fun removeFavoriteAttendanceLogById(id: String): Result<Boolean> {
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
                    BuildingType.MAIN,
                    listOf(
                        PlaceCategory.MAIN.B1,
                        PlaceCategory.MAIN.F1,
                        PlaceCategory.MAIN.F2,
                        PlaceCategory.MAIN.F3,
                    )
                ),
                Building(
                    BuildingType.NEWBUILDING,
                    listOf(
                        PlaceCategory.NEW.F1,
                        PlaceCategory.NEW.F2,
                        PlaceCategory.NEW.F3,
                        PlaceCategory.NEW.F4,
                    )
                ),
                Building(
                    BuildingType.HAKBONG,
                    listOf(
                        Place(
                            "620fcff7a0469d4750585a34",
                            "학봉관 호실",
                            "남호실",
                            "생활관",
                            BuildingType.HAKBONG,
                            Floor.none(),
                            PlaceType.DORMITORY,
                        ),
                        Place(
                            "620fcf9ca0469d4750585a32",
                            "미술실",
                            "미술실",
                            "1층",
                            BuildingType.HAKBONG,
                            Floor.of(1),
                            PlaceType.ETC,
                        ),
                        Place(
                            "620fcfc2a0469d4750585a33",
                            "ATM기",
                            "ATM",
                            "1층",
                            BuildingType.HAKBONG,
                            Floor.of(1),
                            PlaceType.ETC,
                        )
                    ),
                    listOf(
                        Place(
                            "620fcf4ea0469d4750585a31",
                            "학봉관 세탁",
                            "세탁",
                            "세탁하러 오셨나요?",
                            BuildingType.HAKBONG,
                            Floor.none(),
                            PlaceType.LAUNDRY,
                        )
                    )
                ),
                Building(
                    BuildingType.UJEONG,
                    listOf(
                        Place(
                            "620fd105a0469d4750585a37",
                            "우정학사 호실",
                            "여호실",
                            "생활관",
                            BuildingType.UJEONG,
                            Floor.none(),
                            PlaceType.DORMITORY,
                        )
                    ),
                    listOf(
                        Place(
                            "620fc9c3c1ce4101d43d5f98",
                            "우정학사 세탁",
                            "세탁",
                            "세탁하러 오셨나요?",
                            BuildingType.UJEONG,
                            Floor.none(),
                            PlaceType.DORMITORY,
                        )
                    )
                ),
                Building(
                    BuildingType.ETC,
                    listOf(
                        Place(
                            "620fcb32c1ce4101d43d5fa4",
                            "스마트팜",
                            "농장",
                            "지상",
                            BuildingType.ETC,
                            Floor.ground(),
                            PlaceType.FARM,
                        ),
                        Place(
                            "620fcb5ac1ce4101d43d5fa5",
                            "운동장",
                            "운동장",
                            "지상",
                            BuildingType.ETC,
                            Floor.ground(),
                            PlaceType.PLAYGROUND,
                        ),
                        Place(
                            "620fcb6ac1ce4101d43d5fa6",
                            "체육관",
                            "체육관",
                            "지하",
                            BuildingType.ETC,
                            Floor.underground(),
                            PlaceType.GYM,
                        ),
                    ),
                    listOf(PlaceCategory.ETC)
                ),
            )
        )
    }

    companion object {
        private const val TAG = "PlaceRepositoryImpl"
    }
}
