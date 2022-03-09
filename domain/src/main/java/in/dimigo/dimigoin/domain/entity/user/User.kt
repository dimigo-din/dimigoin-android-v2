package `in`.dimigo.dimigoin.domain.entity.user

data class User(
    val _id: String,
    val idx: Int,
    val name: String,
    val grade: Int,
    val `class`: Int,
    val number: Int,
    val serial: Int,
    val photos: List<String>,
    val birthDate: String,
    val libraryId: String,
    val permissions: List<String>,
)
