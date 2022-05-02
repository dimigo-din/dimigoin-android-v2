package `in`.dimigo.dimigoin.data.repository.fake

import `in`.dimigo.dimigoin.domain.entity.meal.Meal
import `in`.dimigo.dimigoin.domain.entity.meal.MealTime
import `in`.dimigo.dimigoin.domain.entity.meal.MealTimes
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import java.time.LocalDate
import java.time.LocalTime

class FakeMealRepository : MealRepository {
    override suspend fun getTodayMeal(): Result<Meal> = Result.success(
        Meal("아침 | 급식 | 입니다", "점심 | 급식 | 입니다", "저녁 | 급식 | 입니다", LocalDate.of(2022, 1, 1))
    )

    override suspend fun getWeeklyMeal(): Result<List<Meal>> = Result.success(
        List(7) { day ->
            val koreanDay = "월화수목금토일"[day] + "요일"
            Meal(
                "$koreanDay | 아침 | 급식 | 입니다",
                "$koreanDay | 점심 | 급식 | 입니다",
                "$koreanDay | 저녁 | 급식 | 입니다",
                LocalDate.of(2022, 1, 1)
            )
        }
    )

    override suspend fun getMealTimeByClass(grade: Int, `class`: Int): Result<MealTime> =
        getMealTimesByGrade(grade).map { it[`class`] }

    override suspend fun getMealTimesByGrade(grade: Int): Result<MealTimes> = Result.success(
        List(6) {
            MealTime(
                it + 1, 6 - it, grade, it,
                LocalTime.of(7, 10).plusMinutes(20L * 3.minus(grade)),
                when (grade) {
                    3 -> LocalTime.of(12, 0).plusMinutes(it * 2L)
                    2 -> LocalTime.of(12, 50).plusMinutes(it * 2L)
                    else -> LocalTime.of(13, 10).plusMinutes(it * 2L)
                },
                LocalTime.of(18, 30).plusMinutes(it * 2L + 3.minus(grade) * 20L)
            )
        }
    )
}