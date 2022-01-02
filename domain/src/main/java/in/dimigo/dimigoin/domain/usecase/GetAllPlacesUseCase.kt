package `in`.dimigo.dimigoin.domain.usecase

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class GetAllPlacesUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke() = placeRepository.getAllPlaces()
}
