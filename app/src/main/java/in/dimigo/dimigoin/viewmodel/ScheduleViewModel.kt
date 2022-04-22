package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.schedule.AnnualSchedule
import `in`.dimigo.dimigoin.domain.entity.schedule.WeeklyTimetable
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.schedule.GetScheduleUseCase
import `in`.dimigo.dimigoin.domain.usecase.schedule.GetTimetableUseCase
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import `in`.dimigo.dimigoin.ui.util.Future
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ScheduleViewModel(
    private val getScheduleUseCase: GetScheduleUseCase,
    private val getTimetableUseCase: GetTimetableUseCase,
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
) : ViewModel() {
    private val _schedule = MutableStateFlow<Future<AnnualSchedule>>(Future.loading())
    val schedule = _schedule.asStateFlow()

    private val _timetable = MutableStateFlow<Future<WeeklyTimetable>>(Future.loading())
    val timetable = _timetable.asStateFlow()

    private val _me = MutableStateFlow<Future<User>>(Future.loading())
    val me = _me.asStateFlow()

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch {
        getMyIdentityUseCase().onSuccess {
            _me.emit(Future.success(it))
        }

        getTimetableUseCase().onSuccess {
            _timetable.emit(Future.success(it))
        }.onFailure {
            _timetable.emit(Future.failure(it))
        }

        getScheduleUseCase().onSuccess {
            _schedule.emit(Future.success(it))
        }.onFailure {
            _schedule.emit(Future.failure(it))
        }
    }
}
