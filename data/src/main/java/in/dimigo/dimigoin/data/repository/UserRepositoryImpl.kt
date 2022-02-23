package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.datasource.LocalSharedPreferenceManager
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class UserRepositoryImpl(
    private val service: DimigoinApiService,
    private val localSharedPreferenceManager: LocalSharedPreferenceManager,
) : UserRepository {

    private var me: User? = null

    override suspend fun login(username: String, password: String) = resultFromCall(
        service.login(LoginRequestModel(username, password))
    ) { response ->
        localSharedPreferenceManager.accessToken = response.accessToken
        localSharedPreferenceManager.refreshToken = response.refreshToken
        true
    }

    override suspend fun me(): Result<User> = resultFromCall(
        service.getMyIdentity(),
        cached = me
    ) { response ->
        response.identity.toEntity().also {
            me = it
        }
    }
}
