package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.user.IdentityResponseModel
import `in`.dimigo.dimigoin.domain.entity.user.User

fun IdentityResponseModel.toEntity() = User(_id, idx, name, grade, `class`, number, serial, photos, birthDate, libraryId, permissions)
