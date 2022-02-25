package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.meal.MealTimes
import `in`.dimigo.dimigoin.domain.usecase.meal.GetGradeMealTimeUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MealTimeViewModel(
    private val getGradeMealTimeUseCase: GetGradeMealTimeUseCase,
) : ViewModel() {
    private val _mealTimes = MutableStateFlow<Future<MealTimes>>(Future.Loading())
    val mealTimes = _mealTimes.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        getGradeMealTimeUseCase().onSuccess {
            _mealTimes.emit(Future.Success(it))
        }.onFailure {
            _mealTimes.emit(Future.Failure(it))
        }
    }
}
