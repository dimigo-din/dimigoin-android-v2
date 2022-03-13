package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.place.*
import `in`.dimigo.dimigoin.ui.util.Future
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.toLowerCase
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
            field = value
            placesMap = value.associateBy(Place::_id)
            viewModelScope.launch {
                _isPlaceLoaded.emit(true)
            }
        }
    private var placesMap: Map<String, Place> = hashMapOf()

    private var myIdentity: User? = null

    private val _currentPlace = MutableStateFlow<Future<Place>>(Future.Nothing())
    val currentPlace = _currentPlace.asStateFlow()

    private val _favoriteAttendanceLogs =
        MutableStateFlow<Future<List<AttendanceLog>>>(Future.Nothing())
    val favoriteAttendanceLog = _favoriteAttendanceLogs.asStateFlow()

    private val _recommendedBuildings = MutableStateFlow<Future<List<Building>>>(Future.Nothing())
    val recommendedBuildings = _recommendedBuildings.asStateFlow()

    private val _isPlaceLoaded = MutableStateFlow(false)
    val isPlaceLoaded = _isPlaceLoaded.asStateFlow()

    val selectedBuilding = mutableStateOf("즐겨찾기")

    init {
        getAllPlaces()
        getCurrentPlace()
        getFavoriteAttendanceLogs()
        getRecommendedBuildings()
    }

    private fun getAllPlaces() = viewModelScope.launch {
        getAllPlacesUseCase().onSuccess { allPlace = it }
    }

    fun getCurrentPlace() = viewModelScope.launch {
        getCurrentPlaceUseCase().onSuccess {
            val cp = it
                ?: allPlace.find { place ->
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

    fun getFilteredPlaceByName(search: String): List<Place> =
        when {
            search.isEmpty() -> emptyList()
            search.isSearchByConsonant() -> filterPlaceByFirstConsonants(allPlace, search)
            else -> filterPlaceByLinearHangul(allPlace, search)
        }

    private fun String.isSearchByConsonant(): Boolean =
        all { it.isConsonant() || it.isDigit() || it.isWhitespace() }

    private fun Char.isConsonant(): Boolean = this in 'ㄱ'..'ㅎ'

    private fun String.toConsonants(): String =
        map { if (it.isSyllable()) FIRST[(it.code - 0xAC00) / 588] else it }.joinToString("")

    private fun filterPlaceByFirstConsonants(allPlace: List<Place>, search: String): List<Place> {
        return allPlace.filter {
            it.name.toConsonants().removeWhitespace().contains(search.removeWhitespace())
        }
    }

    private fun filterPlaceByLinearHangul(allPlace: List<Place>, search: String): List<Place> {
        val linearSearch = search.makeLinear().lowercase().removeWhitespace()
        return allPlace.filter {
            it.name.makeLinear().lowercase().removeWhitespace().contains(linearSearch)
        }
    }

    private fun String.makeLinear(): String =
        map { if (it.isSyllable()) it.makeLinear() else it }.joinToString("")

    private fun Char.makeLinear(): String = (code - 0xAC00).let {
        "${FIRST[it / 588]}${MIDDLE[it % 588 / 28]}${LAST[it % 28]}"
    }

    private fun Char.isSyllable(): Boolean = this in '가'..'힣'

    private fun String.removeWhitespace(): String = this.filterNot { it.isWhitespace() }

    fun placeIdToPlace(placeId: String): Place {
        return placesMap.getOrDefault(placeId, fallbackPlace)
    }

    companion object {
        private const val TAG = "PlaceSelectorViewModel"
        private val fallbackPlace = Place("", "", "", "", "", null, PlaceType.ETC)
        private val FIRST = listOf(
            "ㄱ",
            "ㄲ",
            "ㄴ",
            "ㄷ",
            "ㄸ",
            "ㄹ",
            "ㅁ",
            "ㅂ",
            "ㅃ",
            "ㅅ",
            "ㅆ",
            "ㅇ",
            "ㅈ",
            "ㅉ",
            "ㅊ",
            "ㅋ",
            "ㅌ",
            "ㅍ",
            "ㅎ"
        )
        private val MIDDLE = listOf(
            "ㅏ",
            "ㅐ",
            "ㅑ",
            "ㅒ",
            "ㅓ",
            "ㅔ",
            "ㅕ",
            "ㅖ",
            "ㅗ",
            "ㅘ",
            "ㅙ",
            "ㅚ",
            "ㅛ",
            "ㅜ",
            "ㅝ",
            "ㅞ",
            "ㅟ",
            "ㅠ",
            "ㅡ",
            "ㅢ",
            "ㅣ"
        )
        private val LAST = listOf(
            "",
            "ㄱ",
            "ㄲ",
            "ㄱㅅ",
            "ㄴ",
            "ㄴㅈ",
            "ㄴㅎ",
            "ㄷ",
            "ㄹ",
            "ㄹㄱ",
            "ㄹㅁ",
            "ㄹㅂ",
            "ㄹㅅ",
            "ㄹㅌ",
            "ㄹㅍ",
            "ㄹㅎ",
            "ㅁ",
            "ㅂ",
            "ㅄ",
            "ㅅ",
            "ㅆ",
            "ㅇ",
            "ㅈ",
            "ㅊ",
            "ㅋ",
            "ㅌ",
            "ㅍ",
            "ㅎ"
        )
    }
}
