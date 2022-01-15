package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.Meal
import `in`.dimigo.dimigoin.domain.entity.MealTime
import `in`.dimigo.dimigoin.domain.entity.MealTimes

interface MealRepository {
    suspend fun getTodayMeal(): Result<Meal>
    suspend fun getWeeklyMeal(): Result<List<Meal>>
    suspend fun getMyMealTime(grade: Int, `class`: Int): Result<MealTime>
    suspend fun getMealTimes(grade: Int): Result<MealTimes>
}
