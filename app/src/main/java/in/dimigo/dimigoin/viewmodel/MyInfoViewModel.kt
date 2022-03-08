package `in`.dimigo.dimigoin.viewmodel

import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.usecase.user.GetMyIdentityUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MyInfoViewModel(
    private val getMyIdentityUseCase: GetMyIdentityUseCase,
) : ViewModel() {
    var myIdentity: User? = null

    init {
        getMyIdentity()
    }

    private fun getMyIdentity() = viewModelScope.launch {
        getMyIdentityUseCase().onSuccess { myIdentity = it }
    }
}