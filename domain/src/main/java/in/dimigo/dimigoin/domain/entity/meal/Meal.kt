package `in`.dimigo.dimigoin.domain.entity.meal

import java.time.LocalDate

data class Meal(
    val breakfast: String,
    val lunch: String,
    val dinner: String,
    val date: LocalDate,
)
