package `in`.dimigo.dimigoin.domain.usecase.user

import `in`.dimigo.dimigoin.domain.repository.UserRepository

class GetMyIdentityUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke() = userRepository.getMyIdentity()
}
