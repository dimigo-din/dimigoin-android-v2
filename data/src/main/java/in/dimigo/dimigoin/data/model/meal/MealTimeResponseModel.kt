package `in`.dimigo.dimigoin.data.model.meal

data class MealTimeResponseModel(
    val mealTimes: MealTimes
) {
    data class MealTimes(
        val lunch: List<List<Int>>,
        val dinner: List<List<Int>>,
    )
}
