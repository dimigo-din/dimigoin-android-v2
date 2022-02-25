package `in`.dimigo.dimigoin.domain.usecase.schedule

import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class GetTimetableUseCase(
    private val scheduleRepository: ScheduleRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() =
        userRepository.getMyIdentity().mapCatching {
            scheduleRepository.getTimetableByClass(it.grade, it.`class`).getOrThrow()
        }
}