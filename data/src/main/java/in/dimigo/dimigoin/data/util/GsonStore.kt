package `in`.dimigo.dimigoin.data.util

import `in`.dimigo.dimigoin.data.mapper.FloorAdapter
import `in`.dimigo.dimigoin.data.mapper.PlaceCategoryAdapter
import `in`.dimigo.dimigoin.domain.entity.place.Floor
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory
import com.google.gson.Gson
import com.google.gson.GsonBuilder

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Floor::class.java, FloorAdapter)
    .registerTypeAdapter(Floor.BaseValue::class.java, FloorAdapter)
    .registerTypeAdapter(Floor.Value::class.java, FloorAdapter)
    .registerTypeAdapter(Floor.Underground::class.java, FloorAdapter)
    .registerTypeAdapter(Floor.Ground::class.java, FloorAdapter)
    .registerTypeAdapter(Floor.None::class.java, FloorAdapter)
    .registerTypeAdapter(PlaceCategory::class.java, PlaceCategoryAdapter)
    .registerTypeAdapter(PlaceCategory.FloorCategory::class.java, PlaceCategoryAdapter)
    .registerTypeAdapter(PlaceCategory.NamedCategory::class.java, PlaceCategoryAdapter)
    .registerTypeAdapter(PlaceCategory.None::class.java, PlaceCategoryAdapter)
    .create()
