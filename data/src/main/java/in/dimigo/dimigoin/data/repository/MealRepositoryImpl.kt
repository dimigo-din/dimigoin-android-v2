package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
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

    override suspend fun getTodayMeal(): Result<Meal> =
        Result.success(FAKE_MEAL)

    override suspend fun getWeeklyMeal(): Result<List<Meal>> =
        Result.success(List(7) { FAKE_MEAL })

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
        private val FAKE_MEAL = Meal(
            "현미밥 | 얼큰김칫국 | 토마토달걀볶음 | 호박버섯볶음 | 깍두기 | 베이컨 | 완제김 | 스트링치즈 | 모닝빵미니버거",
            "백미밥 | 강릉식짬뽕순두부 | 치즈순살찜닭 | 콩나물 무침 | 구이김·양념간장 | 총각김치 | 따뜻한 유자차",
            "흑미밥 | 들깨미역국 | 춘권튀김&칠리소스 | 고사리나물 | 포기김치 | 매운돼지갈비찜 | 꿀호떡",
        )
    }
}
