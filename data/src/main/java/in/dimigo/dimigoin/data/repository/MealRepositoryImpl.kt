package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.meal.MealResponseModel
import `in`.dimigo.dimigoin.data.model.meal.MealSequenceResponseModel
import `in`.dimigo.dimigoin.data.model.meal.MealTimeResponseModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.Meal
import `in`.dimigo.dimigoin.domain.entity.MealTime
import `in`.dimigo.dimigoin.domain.entity.MealTimes
import `in`.dimigo.dimigoin.domain.repository.MealRepository
import java.time.LocalDate
import java.time.LocalTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MealRepositoryImpl(
    private val service: DimigoinApiService,
) : MealRepository {

    private var todayMeal: Meal? = null
    private var weeklyMeal: List<Meal>? = null

    override suspend fun getTodayMeal(): Result<Meal> = resultFromCall(
        service.getTodayMeal(),
        cached = todayMeal
    ) { response ->
        response.toEntity().also {
            todayMeal = it
        }
    }

    override suspend fun getWeeklyMeal(): Result<List<Meal>> = resultFromCall(
        service.getWeeklyMeal(),
        cached = weeklyMeal
    ) { response ->
        val now = LocalDate.now()

        List(7) { days ->
            response.meals.map(MealResponseModel::toEntity).find {
                it.date == now.plusDays(days.toLong())
            } ?: FAILED_MEAL
        }.also {
            weeklyMeal = it
        }
    }

    override suspend fun getMealTimeByClass(grade: Int, `class`: Int): Result<MealTime> =
        try {
            Result.success(
                getMealTimes().find {
                    grade == it.grade && `class` == it.`class`
                } ?: throw IllegalArgumentException("Cannot find $grade-$`class`")
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    override suspend fun getMealTimesByGrade(grade: Int): Result<MealTimes> =
        try {
            Result.success(
                getMealTimes().filter {
                    grade == it.grade
                }
            )
        } catch (e: Exception) {
            Result.failure(e)
        }

    private suspend fun getMealTimes(): MealTimes {
        val sequences = fetchMealSequence().mealSequences
        val times = fetchMealTimes().mealTimes

        return List(3) { grade /* indicates actual grade - 1 */ ->
            List(6) { `class` /* indicates actual class - 1 */ ->
                val lunchRank = sequences.lunch[grade].indexOf(`class` + 1)
                val dinnerRank = sequences.dinner[grade].indexOf(`class` + 1)
                MealTime(
                    lunchRank = lunchRank + 1,
                    dinnerRank = dinnerRank + 1,
                    grade = grade + 1,
                    `class` = `class` + 1,
                    breakfastTime = getBreakfastTimeByGrade(grade + 1),
                    lunchTime = times.lunch[grade][lunchRank].toLocalTime(),
                    dinnerTime = times.dinner[grade][dinnerRank].toLocalTime(),
                )
            }
        }.flatten()
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun fetchMealSequence(): MealSequenceResponseModel = withContext(Dispatchers.IO) {
        service.getMealSequence()
            .execute()
            .body()
            ?: throw IllegalStateException("Failed to fetch meal sequence")
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    private suspend fun fetchMealTimes(): MealTimeResponseModel = withContext(Dispatchers.IO) {
        service.getMealTimes()
            .execute()
            .body()
            ?: throw IllegalStateException("Failed to fetch meal sequence")
    }

    private fun Int.toLocalTime() =
        LocalTime.of(this / 100, this % 100)

    private fun getBreakfastTimeByGrade(grade: Int) = when (grade) {
        1 -> LocalTime.of(1, 0)
        2 -> LocalTime.of(2, 0)
        3 -> LocalTime.of(3, 0)
        else -> throw IllegalArgumentException("어떻게 디미고에 ${grade}학년이 있나요...")
    }

    companion object {
        private val FAILED_MEAL = Meal(
            "급식 정보가 없습니다.",
            "급식 정보가 없습니다.",
            "급식 정보가 없습니다.",
            LocalDate.of(1970, 1, 1)
        )
    }
}
