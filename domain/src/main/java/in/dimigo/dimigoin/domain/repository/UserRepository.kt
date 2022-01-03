package `in`.dimigo.dimigoin.domain.repository

interface UserRepository {
    suspend fun login(username: String, password: String): Result<Boolean>
}
