package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val setCurrentPlaceUseCase: SetCurrentPlaceUseCase,
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
) : ViewModel() {

    private var allPlaces: List<Place> = emptyList()
    private var myIdentity: User? = null
    private val _currentPlace = MutableStateFlow<Future<Place>>(Future.Nothing())
    val currentPlace = _currentPlace.asStateFlow()

    init {
        getAllPlaces()
        getMyIdentity()
        getCurrentPlace()
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
                ?: Place("", "${myIdentity?.grade}학년 ${myIdentity?.`class`}반", "", "", "", null, PlaceType.CLASSROOM)
            _currentPlace.emit(Future.Success(cp))
        }.onFailure {
            _currentPlace.emit(Future.Failure(it))
        }
    }

    fun setCurrentPlace(placeType: PlaceType) {
        val place = placeType.toDefaultPlace() ?: return
        viewModelScope.launch {
            setCurrentPlaceUseCase(place._id).onSuccess {
                if (it) { _currentPlace.emit(Future.Success(place)) }
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

    companion object {
        private const val TAG = "LoginViewModel"
    }
}
