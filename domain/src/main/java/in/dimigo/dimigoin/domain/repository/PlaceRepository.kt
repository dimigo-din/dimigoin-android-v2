package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.domain.entity.place.Place

interface PlaceRepository {
    suspend fun getPlaces(): Result<List<Place>>
    suspend fun setCurrentPlace(placeId: String, remark: String?): Result<Boolean>
    suspend fun getCurrentPlace(): Result<Place?>
    suspend fun addFavoriteAttendanceLog(attendanceLog: AttendanceLog): Result<Boolean>
    suspend fun removeFavoriteAttendanceLogById(id: String): Result<Boolean>
    suspend fun getFavoriteAttendanceLogs(): Result<List<AttendanceLog>>
    suspend fun getBuildings(): Result<List<Building>>
}
