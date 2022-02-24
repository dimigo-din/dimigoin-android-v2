package `in`.dimigo.dimigoin.domain.usecase.schedule

import `in`.dimigo.dimigoin.domain.repository.ScheduleRepository

class GetScheduleUseCase(
    private val scheduleRepository: ScheduleRepository,
) {
    suspend operator fun invoke() = scheduleRepository.getSchoolSchedule()
}