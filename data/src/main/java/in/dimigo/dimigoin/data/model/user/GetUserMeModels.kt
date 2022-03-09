package `in`.dimigo.dimigoin.data.model.user

data class GetUserMeResponseModel(
    val identity: IdentityResponseModel
)

data class IdentityResponseModel(
    val _id: String,
    val idx: Int,
    val name: String,
    val grade: Int = 0,
    val `class`: Int = 0,
    val number: Int = 0,
    val serial: Int = 0,
    val photos: List<String> = listOf(),
    val birthDate: String?,
    val libraryId: String?,
    val permissions: List<String> = listOf(),
)
