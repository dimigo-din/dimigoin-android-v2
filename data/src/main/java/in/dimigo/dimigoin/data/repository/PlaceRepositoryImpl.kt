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
                sharedPreferenceManager.favoriteAttendanceLogs + attendanceLog
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
                sharedPreferenceManager.favoriteAttendanceLogs.apply {
                    val elementToRemove = this.find { it._id == id } ?: return Result.success(false)
                    this - elementToRemove
                }
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
        return Result.success(listOf(
            Building("학교", "본관"),
            Building("학교", "신관"),
            Building("생활관", "학봉관"),
            Building("생활관", "우정학사"),
            Building("기타", "그 외"),
        ))
    }
}
