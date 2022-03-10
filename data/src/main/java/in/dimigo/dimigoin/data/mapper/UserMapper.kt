package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.user.IdentityResponseModel
import `in`.dimigo.dimigoin.domain.entity.user.User
import java.time.LocalDate

fun IdentityResponseModel.toEntity() =
    User(name, grade, `class`, number, serial, photos, LocalDate.parse(birthDate), libraryId, permissions)
