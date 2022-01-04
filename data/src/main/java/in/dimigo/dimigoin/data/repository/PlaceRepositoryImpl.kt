package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.data.model.place.PostAttendanceRequestModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class PlaceRepositoryImpl(
    private val service: DimigoinApiService,
) : PlaceRepository {
    private var places: List<Place>? = null
    private var currentPlace: Place? = null

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
        TODO("not implemented")
    }

    override suspend fun removeFavoriteAttendanceLog(id: String): Result<Boolean> {
        TODO("not implemented")
    }

    override suspend fun getFavoriteAttendanceLogs(): Result<List<AttendanceLog>> {
        TODO("not implemented")
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