package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.Meal
import `in`.dimigo.dimigoin.domain.entity.MealTime
import `in`.dimigo.dimigoin.domain.entity.MealTimes
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import java.time.LocalTime

class MealRepositoryImpl(
    private val service: DimigoinApiService,
) : MealRepository {

    private var todayMeal: Meal? = null
    private var weeklyMeal: List<Meal>? = null

    override suspend fun getTodayMeal(): Result<Meal> = resultFromCall(
        service.getTodayMeal(),
        cached = todayMeal
    ) {
        todayMeal = it
        it
    }

    override suspend fun getWeeklyMeal(): Result<List<Meal>> = resultFromCall(
        service.getWeeklyMeal(),
        cached = weeklyMeal
    ) {
        weeklyMeal = it.meals
        it.meals
    }

    override suspend fun getMyMealTime(grade: Int, `class`: Int): Result<MealTime> {
        // TODO temporary implementation
        return Result.success(FAKE_MEAL_TIME.copy(grade = grade, `class` = `class`))
    }

    override suspend fun getMealTimes(grade: Int): Result<MealTimes> {
        // TODO temporary implementation
        return Result.success(
            List(6) {
                FAKE_MEAL_TIME.copy(grade = grade, `class` = it + 1)
            }.associateBy { "${it.grade}학년 ${it.`class`}반" }
        )
    }

    companion object {
        private val FAKE_MEAL_TIME = MealTime(
            order = 1,
            grade = 2,
            `class` = 1,
            breakfastTime = LocalTime.of(7, 20),
            lunchTime = LocalTime.of(7, 20),
            dinnerTime = LocalTime.of(7, 20),
        )
    }
}
