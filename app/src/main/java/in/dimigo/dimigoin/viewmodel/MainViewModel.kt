package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.meal.Meal
import `in`.dimigo.dimigoin.domain.entity.meal.MealTime
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.meal.GetMyMealTimeUseCase
import `in`.dimigo.dimigoin.domain.usecase.meal.GetWeeklyMealUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.theme.Point
import `in`.dimigo.dimigoin.ui.util.Future
import `in`.dimigo.dimigoin.ui.util.asKorean12HoursString
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class MainViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val setCurrentPlaceUseCase: SetCurrentPlaceUseCase,
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
    private val getWeeklyMealUseCase: GetWeeklyMealUseCase,
    private val getMyMealTimeUseCase: GetMyMealTimeUseCase,
) : ViewModel() {

    private var allPlaces: List<Place> = emptyList()
    private var myIdentity: User? = null
    private val _currentPlace = MutableStateFlow<Future<Place>>(Future.Nothing())
    val currentPlace = _currentPlace.asStateFlow()
    private val _weeklyMeal = MutableStateFlow<Future<List<Meal>>>(Future.Loading())
    val weeklyMeal = _weeklyMeal.asStateFlow()
    private val _mealTime = MutableStateFlow<Future<MealTime>>(Future.Loading())
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
                    "",
                    null,
                    PlaceType.CLASSROOM
                )
            _currentPlace.emit(Future.Success(cp))
        }.onFailure {
            _currentPlace.emit(Future.Failure(it))
        }
    }

    fun setCurrentPlace(placeType: PlaceType, callback: (Place) -> Unit) {
        val place = placeType.toDefaultPlace() ?: return
        viewModelScope.launch {
            setCurrentPlaceUseCase(place._id).onSuccess {
                if (it) {
                    _currentPlace.emit(Future.Success(place))
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
                it.building == homeroom.building && it.floor == homeroom.floor && it.name.contains("화장실")
            }
            PlaceType.CORRIDOR -> allPlaces.find {
                it.building == homeroom.building && it.floor == homeroom.floor && it.name.contains("복도")
            }
            PlaceType.TEACHER -> allPlaces.find {
                it.building == homeroom.building && it.name.contains("교무실")
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

    fun getCurrentMealType(page: Int?): String? {
        val timeNow = LocalTime.now()
        if (timeNow.isAfter(LocalTime.of(6, 30)) &&
            timeNow.isBefore(LocalTime.of(8, 20)) ||
            page == 0
        ) {
            return "아침"
        } else if (
            timeNow.isAfter(LocalTime.of(8, 20)) &&
            timeNow.isBefore(LocalTime.of(13, 50)) ||
            page == 1
        ) {
            return "점심"
        } else if (
            timeNow.isAfter(LocalTime.of(13, 50)) &&
            timeNow.isBefore(LocalTime.of(19, 50)) ||
            page == 2
        ) {
            return "저녁"
        } else {
            return null
        }
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
