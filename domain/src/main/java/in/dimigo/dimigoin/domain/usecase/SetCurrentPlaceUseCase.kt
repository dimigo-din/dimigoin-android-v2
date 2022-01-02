package `in`.dimigo.dimigoin.domain.usecase

import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class SetCurrentPlaceUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(place: Place, remark: String = "") = invoke(place._id, remark)
    suspend operator fun invoke(placeId: String, remark: String = "") = placeRepository.setCurrentPlace(placeId)
}
