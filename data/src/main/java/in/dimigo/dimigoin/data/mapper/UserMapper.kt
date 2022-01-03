package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.user.IdentityResponseModel
import `in`.dimigo.dimigoin.domain.entity.User

fun IdentityResponseModel.toEntity() = User(name, grade, `class`, number, serial)
