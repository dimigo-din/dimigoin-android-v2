package `in`.dimigo.dimigoin.domain.usecase.meal

import `in`.dimigo.dimigoin.domain.entity.MealTime
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class GetMyMealTimeUseCase(
    private val mealRepository: MealRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): Result<MealTime> =
        userRepository.me().mapCatching {
            mealRepository.getMealTimeByClass(it.grade, it.`class`).getOrThrow()
        }
}
