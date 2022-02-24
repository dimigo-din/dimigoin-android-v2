package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.meal.MealResponseModel
import `in`.dimigo.dimigoin.domain.entity.Meal
import java.time.LocalDate

fun MealResponseModel.toEntity() = Meal(
    breakfast = breakfast,
    lunch = lunch,
    dinner = dinner,
    date = LocalDate.parse(date)
)
