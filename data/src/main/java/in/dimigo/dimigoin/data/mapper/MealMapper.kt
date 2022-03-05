package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.meal.MealResponseModel
import `in`.dimigo.dimigoin.domain.entity.meal.Meal
import java.time.LocalDate

fun MealResponseModel.toEntity() = Meal(
    breakfast = breakfast.joinOrNotExists(),
    lunch = lunch.joinOrNotExists(),
    dinner = dinner.joinOrNotExists(),
    date = LocalDate.parse(date)
)

fun List<String>.joinOrNotExists() =
    if (isEmpty()) {
        "급식 정보가 없습니다."
    } else {
        joinToString(" | ")
    }
