package `in`.dimigo.dimigoin.data.util

import `in`.dimigo.dimigoin.data.mapper.FloorAdapter
import `in`.dimigo.dimigoin.domain.entity.place.Floor
import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Floor::class.java, FloorAdapter())
    .create()
