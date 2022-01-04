package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place

interface PlaceRepository {

    /**
     * Get all available places.
     *
     * @return list of places
     */
    suspend fun getAllPlaces(): Result<List<Place>>

    /**
     * Set current place.
     *
     * @return true if the request was successful
     */
    suspend fun setCurrentPlace(placeId: String, remark: String?): Result<Boolean>

    /**
     * Get current place.
     *
     * @return current place
     */
    suspend fun getCurrentPlace(): Result<Place?>

    /**
     * Mark the place as favorite.
     *
     * @return true if the request was successful
     */
    suspend fun addFavoriteAttendanceLog(attendanceLog: AttendanceLog): Result<Boolean>

    /**
     * Remove favorite from the place.
     *
     * @return true if the request was successful
     */
    suspend fun removeFavoriteAttendanceLog(id: String): Result<Boolean>

    /**
     * Get all favorite places.
     *
     * @return favorite places
     */
    suspend fun getFavoriteAttendanceLogs(): Result<List<AttendanceLog>>

    /**
     * Get buildings.
     *
     * @return buildings
     */
    suspend fun getBuildings(): Result<List<Building>>
}
