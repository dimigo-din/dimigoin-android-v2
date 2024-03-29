package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class AddFavoriteAttendanceLogUseCase(
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(attendanceLog: AttendanceLog) = placeRepository.addFavoriteAttendanceLog(attendanceLog)
}
