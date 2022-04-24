package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.domain.entity.place.Floor
import com.google.gson.*
import java.lang.reflect.Type

class FloorAdapter : JsonSerializer<Floor>, JsonDeserializer<Floor> {
    override fun serialize(
        src: Floor,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        val str = when (src) {
            is Floor.Underground -> "u"
            is Floor.Ground -> "g"
            is Floor.None -> "n"
            is Floor.Value -> src.value.toString()
            is Floor.BaseValue -> (1 - src.value).toString()
        }

        return JsonPrimitive(str)
    }

    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Floor {
        val s = json.asString
        return when (json.asString.first()) {
            'u' -> Floor.underground()
            'g' -> Floor.ground()
            'n' -> Floor.none()
            else -> Floor.of(s.toInt())
        }
    }
}