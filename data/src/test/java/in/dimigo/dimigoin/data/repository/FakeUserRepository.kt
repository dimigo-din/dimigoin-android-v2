package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.domain.entity.user.User
import `in`.dimigo.dimigoin.domain.repository.UserRepository
import java.time.LocalDate

class FakeUserRepository : UserRepository {
    override suspend fun login(username: String, password: String): Result<Boolean> =
        Result.success(username == FAKE_USERNAME && password == FAKE_PASSWORD)

    override suspend fun getMyIdentity(): Result<User> = Result.success(FAKE_USER)

    companion object {
        const val FAKE_USERNAME = "test_account"
        const val FAKE_PASSWORD = "password!!"
        val FAKE_USER = User(
            "테스트",
            1, 1, 1, 1101,
            photos = listOf("https://example.com/photo1", "https://example.com/photo2"),
            birthdate = LocalDate.of(2006, 1, 1),
            libraryId = "12345678",
            permissions = emptyList()
        )
    }
}