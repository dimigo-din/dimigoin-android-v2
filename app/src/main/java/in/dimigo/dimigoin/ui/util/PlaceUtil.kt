package `in`.dimigo.dimigoin.ui.util

import `in`.dimigo.dimigoin.R
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType

val PlaceType.icon
    get() = when (this) {
        PlaceType.CLASSROOM -> R.drawable.ic_classroom
        PlaceType.RESTROOM -> R.drawable.ic_restroom
        PlaceType.CIRCLE, PlaceType.AFTERSCHOOL -> R.drawable.ic_circle_afterschool
        PlaceType.TEACHER -> R.drawable.ic_teacher_room
        PlaceType.CORRIDOR -> R.drawable.ic_corridor
        PlaceType.FARM -> R.drawable.ic_farm
        PlaceType.PLAYGROUND -> R.drawable.ic_playground
        PlaceType.GYM -> R.drawable.ic_gym
        PlaceType.LAUNDRY -> R.drawable.ic_laundry
        PlaceType.SCHOOL -> R.drawable.ic_school
        PlaceType.DORMITORY -> R.drawable.ic_dormitory
        PlaceType.ETC, PlaceType.ABSENT -> R.drawable.ic_idk_room
    }
