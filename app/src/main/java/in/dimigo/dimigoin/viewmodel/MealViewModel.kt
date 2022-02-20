package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.Meal
import `in`.dimigo.dimigoin.domain.entity.MealTime
import `in`.dimigo.dimigoin.domain.usecase.meal.GetMyMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetWeeklyMealUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealViewModel(
    private val getWeeklyMealUseCase: GetWeeklyMealUseCase,
    private val getMyMealTimeUseCase: GetMyMealTimeUseCase,
) : ViewModel() {
    private val _weeklyMeal = MutableStateFlow<Future<List<Meal>>>(Future.Loading())
    val weeklyMeal = _weeklyMeal.asStateFlow()

    private val _mealTime = MutableStateFlow<Future<MealTime>>(Future.Loading())
    val mealTime = _mealTime.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        getWeeklyMealUseCase().onSuccess {
            _weeklyMeal.emit(Future.Success(it))
        }.onFailure {
            _weeklyMeal.emit(Future.Failure(it))
        }

        getMyMealTimeUseCase().onSuccess {
            _mealTime.emit(Future.Success(it))
        }.onFailure {
            _mealTime.emit(Future.Failure(it))
        }
    }
}
