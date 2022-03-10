package `in`.dimigo.dimigoin.domain.entity.user

import java.time.LocalDate

data class User(
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
