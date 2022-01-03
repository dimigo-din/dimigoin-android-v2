package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place

interface PlaceRepository {

    /**
     * Get all available places.
     *
     * @return list of places
     */
    suspend fun getAllPlaces(): List<Place>

    /**
     * Set current place.
     *
     * @return true if the request was successful
     */
    suspend fun setCurrentPlace(placeId: String, remark: String?): Boolean

    /**
     * Get current place.
     *
     * @return current place
     */
    suspend fun getCurrentPlace(): Place?

    /**
     * Mark the place as favorite.
     *
     * @return true if the request was successful
     */
    suspend fun addFavoritePlace(placeId: String): Boolean

    /**
     * Remove favorite from the place.
     *
     * @return true if the request was successful
     */
    suspend fun removeFavoritePlace(placeId: String): Boolean

    /**
     * Get all favorite places.
     *
     * @return favorite places
     */
    suspend fun getFavoritePlacesId(): List<String>

    /**
     * Get buildings ordered by grade.
     *
     * @return buildings
     */
    suspend fun getRecommendedBuildingsByGrade(grade: Int): List<Building>
}
