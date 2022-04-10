package `in`.dimigo.dimigoin.data.model.meal

data class TodayMealResponseModel(
    val meal: MealResponseModel
)

data class MealResponseModel(
    val breakfast: List<String>,
    val lunch: List<String>,
    val dinner: List<String>,
    val date: String,
)
