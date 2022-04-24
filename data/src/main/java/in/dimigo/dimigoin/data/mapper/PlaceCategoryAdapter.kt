package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.domain.entity.place.BuildingType
import `in`.dimigo.dimigoin.domain.entity.place.Floor
import `in`.dimigo.dimigoin.domain.entity.place.PlaceCategory
import com.google.gson.*
import java.lang.reflect.Type

object PlaceCategoryAdapter : JsonSerializer<PlaceCategory>, JsonDeserializer<PlaceCategory> {
    override fun serialize(
        src: PlaceCategory,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonObject().apply {
            when (src) {
                is PlaceCategory.FloorCategory -> {
                    addProperty("t", "f")
                    add("b", context.serialize(src.building))
                    add("f", context.serialize(src.floor))
                }
                is PlaceCategory.NamedCategory -> {
                    addProperty("t", "n")
                    addProperty("n", src.name)
                }
                PlaceCategory.None -> addProperty("t", "no")
            }
            addProperty("d", src.description)
        }
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PlaceCategory {
        return json.asJsonObject.let {
            val d = it.get("d").asString
            when (it.get("t").asString) {
                "f" -> {
                    val b = context.deserialize<BuildingType>(it.get("b"), BuildingType::class.java)
                    val f = context.deserialize<Floor>(it.get("f"), Floor::class.java)
                    PlaceCategory.from(b, f)
                }
                "n" -> {
                    val n = it.get("n").asString
                    if (n == "기타 장소 및 사유" && d == "저는 다른 곳에 있어요") {
                        PlaceCategory.ETC
                    } else {
                        PlaceCategory.named(n, d)
                    }
                }
                else -> {
                    PlaceCategory.None
                }
            }
        }
    }
}
