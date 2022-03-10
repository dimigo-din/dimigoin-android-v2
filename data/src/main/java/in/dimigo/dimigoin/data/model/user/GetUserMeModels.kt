package `in`.dimigo.dimigoin.data.model.user

import java.time.LocalDate

data class GetUserMeResponseModel(
    val identity: IdentityResponseModel
)

data class IdentityResponseModel(
    val _id: String,
    val idx: Int,
    val name: String,
    val grade: Int,
    val `class`: Int,
    val number: Int,
    val serial: Int,
    val photos: List<String>,
    val birthDate: LocalDate,
    val libraryId: String,
    val permissions: List<String>,
)
