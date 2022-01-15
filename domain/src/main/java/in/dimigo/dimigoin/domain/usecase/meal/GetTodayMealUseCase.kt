package `in`.dimigo.dimigoin.domain.usecase.meal

import `in`.dimigo.dimigoin.domain.repository.MealRepository

class GetTodayMealUseCase(
    private val mealRepository: MealRepository,
) {
    suspend operator fun invoke() = mealRepository.getTodayMeal()
}
