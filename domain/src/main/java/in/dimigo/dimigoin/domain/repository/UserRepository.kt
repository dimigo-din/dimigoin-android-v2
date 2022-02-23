package `in`.dimigo.dimigoin.domain.repository

import `in`.dimigo.dimigoin.domain.entity.user.User

interface UserRepository {
    suspend fun login(username: String, password: String): Result<Boolean>
    suspend fun getMyIdentity(): Result<User>
}
