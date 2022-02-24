package `in`.dimigo.dimigoin.domain.entity.meal

import java.time.LocalTime

typealias MealTimes = Map<String, MealTime>

data class MealTime(
    val order: Int,
    val grade: Int,
    val `class`: Int,
    val breakfastTime: LocalTime,
    val lunchTime: LocalTime,
    val dinnerTime: LocalTime,
)
