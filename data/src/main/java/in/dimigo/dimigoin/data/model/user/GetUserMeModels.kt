package `in`.dimigo.dimigoin.data.model.user

data class GetUserMeResponseModel(
    val identity: IdentityResponseModel
)

data class IdentityResponseModel(
    val name: String,
    val grade: Int,
    val `class`: Int,
    val number: Int,
    val serial: Int,
    val photos: List<String>,
    val birthdate: String,
    val libraryId: String?,
    val permissions: List<String>,
)
