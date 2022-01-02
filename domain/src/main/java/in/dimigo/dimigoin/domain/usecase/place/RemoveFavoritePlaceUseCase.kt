package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class RemoveFavoritePlaceUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(place: Place) = invoke(place._id)
    suspend operator fun invoke(placeId: String) = placeRepository.removeFavoritePlace(placeId)
}