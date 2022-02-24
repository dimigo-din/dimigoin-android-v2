package `in`.dimigo.dimigoin.domain.usecase.meal

import `in`.dimigo.dimigoin.domain.entity.meal.MealTime
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class GetMyMealTimeUseCase(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<MealTime> =
        userRepository.getMyIdentity().mapCatching {
            mealRepository.getMealTimeByClass(it.grade, it.`class`).getOrThrow()
        }
}
