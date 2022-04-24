package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.meal.Meal
import `in`.dimigo.dimigoin.domain.entity.meal.MealTime
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.meal.GetMyMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetTodayMealUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.composables.MealTimeType
import `in`.dimigo.dimigoin.ui.util.Future
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime

class MainViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val setCurrentPlaceUseCase: SetCurrentPlaceUseCase,
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
    private val getTodayMealUseCase: GetTodayMealUseCase,
    private val getMyMealTimeUseCase: GetMyMealTimeUseCase,
) : ViewModel() {

    private var allPlaces: List<Place> = emptyList()
    private var myIdentity: User? = null
    private val _currentPlace = MutableStateFlow<Future<Place>>(Future.loading())
    val currentPlace = _currentPlace.asStateFlow()
    private val _todayMeal = MutableStateFlow<Future<Meal>>(Future.loading())
    val todayMeal = _todayMeal.asStateFlow()
    private val _mealTime = MutableStateFlow<Future<MealTime>>(Future.loading())
    val mealTime = _mealTime.asStateFlow()

    init {
        getAllPlaces()
        getMyIdentity()
        getCurrentPlace()
        fetch()
    }

    private fun getAllPlaces() = viewModelScope.launch {
        getAllPlacesUseCase().onSuccess { allPlaces = it }
    }

    private fun getMyIdentity() = viewModelScope.launch {
        getMyIdentityUseCase().onSuccess { myIdentity = it }
    }

    fun getCurrentPlace() = viewModelScope.launch {
        getCurrentPlaceUseCase().onSuccess {
            val cp = it
                ?: allPlaces.find { place ->
                    place.name == "${myIdentity?.grade}학년 ${myIdentity?.`class`}반"
                }
                ?: Place(
                    "",
                    "${myIdentity?.grade}학년 ${myIdentity?.`class`}반",
                    "",
                    "",
                    PlaceCategory.None,
                    PlaceType.CLASSROOM
                )
            _currentPlace.emit(Future.success(cp))
        }.onFailure {
            _currentPlace.emit(Future.failure(it))
        }
    }

    fun setCurrentPlace(placeType: PlaceType, callback: (Place) -> Unit) {
        val place = placeType.toDefaultPlace() ?: return
        viewModelScope.launch {
            setCurrentPlaceUseCase(place._id).onSuccess {
                if (it) {
                    _currentPlace.emit(Future.success(place))
                    callback(place)
                }
            }
        }
    }

    private fun PlaceType.toDefaultPlace(): Place? {
        val homeroom = getHomeroom() ?: return null

        return when (this) {
            PlaceType.CLASSROOM -> homeroom
            PlaceType.RESTROOM -> allPlaces.find {
                it.placeCategory == homeroom.placeCategory && it.name.contains("화장실")
            }
            PlaceType.CORRIDOR -> allPlaces.find {
                it.placeCategory == homeroom.placeCategory && it.name.contains("복도")
            }
            PlaceType.TEACHER -> allPlaces.find {
                val pc = it.placeCategory as? PlaceCategory.FloorCategory
                val hr = homeroom.placeCategory as? PlaceCategory.FloorCategory
                pc?.building == hr?.building && it.name.contains("교무실")
            }
            else -> null
        }
    }

    fun getHomeroom(): Place? {
        return allPlaces.find {
            it.name == "${myIdentity?.grade}학년 ${myIdentity?.`class`}반"
        }
    }

    private fun fetch() = viewModelScope.launch {
        getTodayMealUseCase().onSuccess {
            _todayMeal.emit(Future.success(it))
        }.onFailure {
            _todayMeal.emit(Future.failure(it))
        }

        getMyMealTimeUseCase().onSuccess {
            _mealTime.emit(Future.success(it))
        }.onFailure {
            _mealTime.emit(Future.failure(it))
        }
    }

    fun getCurrentMealType(): Int {
        val timeNow = LocalTime.now()
        return when {
            MealTimeType.BREAKFAST.timeRange.contains(timeNow) -> 0
            MealTimeType.LUNCH.timeRange.contains(timeNow) -> 1
            MealTimeType.DINNER.timeRange.contains(timeNow) -> 2
            else -> 0
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}
