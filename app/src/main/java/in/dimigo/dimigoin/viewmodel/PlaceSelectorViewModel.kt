package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.User
import `in`.dimigo.dimigoin.domain.usecase.place.AddFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetFavoriteAttendanceLogsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetRecommendedBuildingsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.RemoveFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaceSelectorViewModel(
    private val getAllPlacesUseCase: GetAllPlacesUseCase,
    private val getCurrentPlaceUseCase: GetCurrentPlaceUseCase,
    private val setCurrentPlaceUseCase: SetCurrentPlaceUseCase,
    private val addFavoriteAttendanceLogUseCase: AddFavoriteAttendanceLogUseCase,
    private val removeFavoriteAttendanceLogUseCase: RemoveFavoriteAttendanceLogUseCase,
    private val getFavoriteAttendanceLogsUseCase: GetFavoriteAttendanceLogsUseCase,
    private val getRecommendedBuildingsUseCase: GetRecommendedBuildingsUseCase,
) : ViewModel() {

    private var allPlace: List<Place> = emptyList()
        set(value) {
            Log.d(TAG, "allPlaces: 0")
            field = value
            Log.d(TAG, "allPlaces: 1")
            placesMap = value.associateBy(Place::_id)
            Log.d(TAG, "allPlaces: $placesMap")
        }
    private var placesMap: Map<String, Place> = hashMapOf()

    private var myIdentity: User? = null

    private val _currentPlace = MutableStateFlow<Future<Place>>(Future.Nothing())
    val currentPlace = _currentPlace.asStateFlow()

    private val _favoriteAttendanceLogs = MutableStateFlow<Future<List<AttendanceLog>>>(Future.Nothing())
    val favoriteAttendanceLog = _favoriteAttendanceLogs.asStateFlow()

    private val _recommendedBuildings = MutableStateFlow<Future<List<Building>>>(Future.Nothing())
    val recommendedBuildings = _recommendedBuildings.asStateFlow()

    var selectedBuilding = mutableStateOf("즐겨찾기")

    init {
        getAllPlaces()
        getCurrentPlace()
        getFavoriteAttendanceLogs()
        getRecommendedBuildings()
    }

    private fun getAllPlaces() = viewModelScope.launch {
        getAllPlacesUseCase().onSuccess { allPlace = it }
    }

    private fun getCurrentPlace() = viewModelScope.launch {
        getCurrentPlaceUseCase().onSuccess {
            val cp = it
                ?: allPlace.find { place ->
                    place.name == "${myIdentity?.grade}학년 ${myIdentity?.`class`}반"
                }
                ?: Place("", "${myIdentity?.grade}학년 ${myIdentity?.`class`}반", "", "", "", null, PlaceType.CLASSROOM)
            _currentPlace.emit(Future.Success(cp))
        }.onFailure {
            _currentPlace.emit(Future.Failure(it))
        }
    }

    fun getAllPlace(): List<Place> {
        return allPlace
    }

    fun setCurrentPlace(
        place: Place,
        remark: String?,
        callback: (Place, String?) -> Unit,
    ) = viewModelScope.launch {
        setCurrentPlaceUseCase(place._id, remark).onSuccess {
            if (it) {
                _currentPlace.emit(Future.Success(place))
                callback(place, remark)
            }
        }
    }

    fun addFavoriteAttendanceLog(
        place: Place,
        remark: String?,
        callback: (Place, String?) -> Unit,
    ) = viewModelScope.launch {
        addFavoriteAttendanceLogUseCase(AttendanceLog(place._id, remark)).onSuccess {
            if (it) {
                getFavoriteAttendanceLogs()
                callback(place, remark)
            }
        }
    }

    private fun getFavoriteAttendanceLogs() = viewModelScope.launch {
        getFavoriteAttendanceLogsUseCase().onSuccess {
            _favoriteAttendanceLogs.emit(Future.Success(it))
        }
    }

    fun removeFavoriteAttendanceLog(
        attendanceLog: AttendanceLog,
        callback: (Place) -> Unit,
    ) = viewModelScope.launch {
        removeFavoriteAttendanceLogUseCase(attendanceLog._id).onSuccess {
            if (it) {
                getFavoriteAttendanceLogs()
                callback(placeIdToPlace(attendanceLog.placeId))
            }
        }
    }

    private fun getRecommendedBuildings() = viewModelScope.launch {
        getRecommendedBuildingsUseCase().onSuccess {
            _recommendedBuildings.emit(Future.Success(it))
        }
    }

    fun getFilteredPlaceByCategory(category: String): List<Place> {
        return allPlace.filter { "${it.building} ${it.floor}" == category }
    }

    fun getFilteredPlaceByName(search: String): List<Place> {
        return allPlace.filter {
            it.name.contains(search).or(it._id.contains(search))
        }
    }

    fun placeIdToPlace(placeId: String): Place {
        return placesMap.getOrDefault(placeId, fallbackPlace)
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private val fallbackPlace = Place("", "", "", "", "", null, PlaceType.ETC)
    }
}
