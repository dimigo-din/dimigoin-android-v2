package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class GetAllPlacesUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke() = placeRepository.getAllPlaces()
}
