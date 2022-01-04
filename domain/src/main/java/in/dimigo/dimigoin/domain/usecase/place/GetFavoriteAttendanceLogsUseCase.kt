package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class GetFavoriteAttendanceLogsUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke() = placeRepository.getFavoriteAttendanceLogs()
}
