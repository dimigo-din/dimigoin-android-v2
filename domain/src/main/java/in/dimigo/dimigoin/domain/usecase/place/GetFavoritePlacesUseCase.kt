package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class GetFavoritePlacesUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke() = placeRepository.getFavoritePlacesId()
}