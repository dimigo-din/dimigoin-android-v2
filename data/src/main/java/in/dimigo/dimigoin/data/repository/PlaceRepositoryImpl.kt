package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.place.AttendanceLogRequestModel
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class PlaceRepositoryImpl(
    private val service: DimigoinApiService,
) : PlaceRepository {
    private val placeMutex = Mutex()
    private var places: List<Place>? = null
    private var currentPlace: Place? = null

    override suspend fun getAllPlaces(): List<Place> {
        val response = service.getPlaces()

        return if (response.isSuccessful) {
            placeMutex.withLock {
                places = response.body()?.places?.map { it.toEntity() }
            }
            placeMutex.withLock { places!! }
        } else {
            throw IllegalStateException(response.errorBody()?.string())
        }
    }

    override suspend fun setCurrentPlace(placeId: String, remark: String?): Boolean {
        val request = AttendanceLogRequestModel(placeId, remark)
        val response = service.addAttendanceLog(request)

        return if (response.isSuccessful) {
            placeMutex.withLock {
                currentPlace = response.body()?.place?.toEntity()
            }
            placeMutex.withLock { currentPlace?._id == placeId }
        } else {
            throw IllegalStateException(response.errorBody()?.string())
        }
    }

    override suspend fun getCurrentPlace(): Place? {
        val response = service.getAttendanceLogs()

        return if (response.isSuccessful) {
            placeMutex.withLock {
                currentPlace = response.body()?.logs?.first()?.place?.toEntity()
            }
            placeMutex.withLock { currentPlace }
        } else {
            throw IllegalStateException(response.errorBody()?.string())
        }
    }

    override suspend fun addFavoritePlace(placeId: String): Boolean {
        TODO("not implemented")
    }

    override suspend fun removeFavoritePlace(placeId: String): Boolean {
        TODO("not implemented")
    }

    override suspend fun getFavoritePlacesId(): List<String> {
        TODO("not implemented")
    }

    override suspend fun getRecommendedBuildingsByGrade(grade: Int): List<Building> {
        return listOf(
            Building("학교", "본관"),
            Building("학교", "신관"),
            Building("생활관", "학봉관"),
            Building("생활관", "우정학사"),
            Building("기타", "그 외"),
        )
    }

}