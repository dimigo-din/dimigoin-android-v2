package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.util.SessionDataStore
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryImpl(
    private val service: DimigoinApiService,
) : UserRepository {
    private val accessTokenMutex = Mutex()

    override suspend fun login(username: String, password: String) = resultFromCall(
        service.login(LoginRequestModel(username, password))
    ) {
        SessionDataStore.accessToken = it.accessToken
        true
    }
}
