package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class SetCurrentPlaceUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(placeId: String, remark: String? = null) = placeRepository.setCurrentPlace(placeId, remark)
}
