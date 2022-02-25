package `in`.dimigo.dimigoin.data.model.meal

data class MealSequenceResponseModel(
    val mealSequences: MealSequences
) {
    data class MealSequences(
        val lunch: List<List<Int>>,
        val dinner: List<List<Int>>,
    )
}
