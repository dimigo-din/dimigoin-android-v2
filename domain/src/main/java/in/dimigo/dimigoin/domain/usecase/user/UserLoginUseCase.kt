package `in`.dimigo.dimigoin.domain.usecase.user

import `in`.dimigo.dimigoin.domain.repository.UserRepository

class UserLoginUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(username: String, password: String) = userRepository.login(username, password)
}
