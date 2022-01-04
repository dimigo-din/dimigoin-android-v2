package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class RemoveFavoriteAttendanceLogUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(id: String) =
        placeRepository.removeFavoriteAttendanceLog(id)
}
