package `in`.dimigo.dimigoin.domain.usecase.place

import `in`.dimigo.dimigoin.domain.entity.place.Building
import `in`.dimigo.dimigoin.domain.repository.PlaceRepository

class GetRecommendedBuildingsUseCase(
    /* TODO add user repository to get grade. */
    private val placeRepository: PlaceRepository,
) {
    suspend operator fun invoke(): Result<List<Building>> {
        /* TODO get user grade */
        val grade = 1
        return placeRepository.getBuildings()
    }
}
