package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.util.SessionDataStore
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class UserRepositoryImpl(
    private val service: DimigoinApiService,
) : UserRepository {
    private val accessTokenMutex = Mutex()

    override suspend fun login(username: String, password: String): Boolean {
        val response = service.login(LoginRequestModel(username, password))

        return if (response.isSuccessful) {
            accessTokenMutex.withLock {
                SessionDataStore.accessToken = response.body()?.accessToken
            }
            true
        } else {
            false
        }
    }
}
