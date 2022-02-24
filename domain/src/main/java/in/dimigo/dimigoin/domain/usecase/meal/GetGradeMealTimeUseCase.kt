package `in`.dimigo.dimigoin.domain.usecase.meal

import `in`.dimigo.dimigoin.domain.entity.meal.MealTimes
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class GetGradeMealTimeUseCase(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<MealTimes> =
        userRepository.getMyIdentity().mapCatching {
            mealRepository.getMealTimes(it.grade).getOrThrow()
        }
}
