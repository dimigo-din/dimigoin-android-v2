package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.Building
import `in`.dimigo.dimigoin.domain.entity.Place
import `in`.dimigo.dimigoin.domain.entity.PlaceCategory
import `in`.dimigo.dimigoin.domain.entity.User
import `in`.dimigo.dimigoin.domain.usecase.place.AddFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetAllPlacesUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetFavoriteAttendanceLogsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.GetRecommendedBuildingsUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.RemoveFavoriteAttendanceLogUseCase
import `in`.dimigo.dimigoin.domain.usecase.place.SetCurrentPlaceUseCase
import `in`.dimigo.dimigoin.ui.util.Future
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

    private var allPlaces: List<Place> = emptyList()
        set(value) {
            field = value
            placesMap = value.associateBy(Place::_id)
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
        getAllPlacesUseCase().onSuccess { allPlaces = it }
    }

    private fun getCurrentPlace() = viewModelScope.launch {
        getCurrentPlaceUseCase().onSuccess {
            val cp = it
                ?: allPlaces.find { place ->
                    place.name == "${myIdentity?.grade}학년 ${myIdentity?.`class`}반"
                }
                ?: Place("", "${myIdentity?.grade}학년 ${myIdentity?.`class`}반", "", null, "교실")
            _currentPlace.emit(Future.Success(cp))
        }.onFailure {
            _currentPlace.emit(Future.Failure(it))
        }
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
        }.onFailure {
            _recommendedBuildings.emit(Future.Success(defaultBuildings))
        }
    }

    fun getFilteredPlaces(category: String): List<Place> {
        return allPlaces.filter { "${it.building} ${it.floor}" == category }
    }

    fun placeIdToPlace(placeId: String): Place {
        return placesMap.getOrDefault(placeId, fallbackPlace)
    }

    companion object {
        private const val TAG = "LoginViewModel"
        private val defaultBuildings = listOf(
            Building(
                "학교", "본관",
                listOf(
                    PlaceCategory("B1층", "급식실"),
                    PlaceCategory("1층", "교무실, 교실"),
                    PlaceCategory("2층", "교실, 특별실"),
                    PlaceCategory("3층", "동아리실, 특별실"),
                    PlaceCategory("4층", "옥상, 골프장"),
                )
            ),
            Building(
                "학교", "신관",
                listOf(
                    PlaceCategory("1층", "교무실, 특별실"),
                    PlaceCategory("2층", "교실, 특별실"),
                    PlaceCategory("3층", "자습실, 도서관"),
                    PlaceCategory("4층", "대강당"),
                )
            ),
            Building("생활관", "학봉관", listOf(
                Place(
                    "603c58898703c000245b13d7",
                    "학봉관",
                    "학봉관",
                    null,
                    "학봉관",
                )
            )),
            Building("생활관", "우정학사", listOf(
                Place(
                    "603c58898703c000245b13d8",
                    "우정학사",
                    "우정학사",
                    null,
                    "우정학사",
                )
            )),
            Building("기타", "그 외", listOf(
                Place(
                    "60436dfb5745e110d9713536",
                    "스마트팜",
                    "지상",
                    null,
                    "지상",
                ),
                Place(
                    "60436dfb5745e110d9713537",
                    "운동장",
                    "지상",
                    null,
                    "지상",
                ),
                Place(
                    "60436dfb5745e110d9713538",
                    "체육관",
                    "지하",
                    null,
                    "지하",
                ),
            )),
        )
        private val fallbackPlace = Place("", "", "", "", "")
    }
}
