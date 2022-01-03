package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.datasource.DimigoinApiService
import `in`.dimigo.dimigoin.data.mapper.toEntity
import `in`.dimigo.dimigoin.data.model.user.LoginRequestModel
import `in`.dimigo.dimigoin.data.util.SessionDataStore
import `in`.dimigo.dimigoin.data.util.resultFromCall
import `in`.dimigo.dimigoin.domain.entity.User
import `in`.dimigo.dimigoin.domain.repository.UserRepository

class UserRepositoryImpl(
    private val service: DimigoinApiService,
) : UserRepository {

    override suspend fun login(username: String, password: String) = resultFromCall(
        service.login(LoginRequestModel(username, password))
    ) { response ->
        SessionDataStore.accessToken = response.accessToken
        true
    }

    override suspend fun me(): Result<User> = resultFromCall(
        service.getMyIdentity()
    ) { response ->
        response.identity.toEntity()
    }
}
