package `in`.dimigo.dimigoin.domain.entity

import java.time.LocalTime

typealias MealTimes = List<MealTime>

data class MealTime(
    val lunchRank: Int,
    val dinnerRank: Int,
    val grade: Int,
    val `class`: Int,
    val breakfastTime: LocalTime,
    val lunchTime: LocalTime,
    val dinnerTime: LocalTime,
)
