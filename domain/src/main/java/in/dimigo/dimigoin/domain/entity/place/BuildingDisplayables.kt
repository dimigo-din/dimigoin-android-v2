package `in`.dimigo.dimigoin.domain.entity.place

interface BuildingDisplayable {
    fun getName(): String
}

object FavoritePlaces : BuildingDisplayable {
    override fun getName(): String = "즐겨찾기"
}

sealed class Building(
    open val type: BuildingType,
    open val children: List<PlaceDisplayable>,
) : BuildingDisplayable {

    override fun getName(): String = type.value

    open class Default(
        override val type: BuildingType,
        override val children: List<PlaceDisplayable>,
    ) : Building(type, children)

    data class Value(
        override val type: BuildingType,
        override val children: List<PlaceDisplayable>,
    ) : Default(type, children)

    data class ValueWithExtra(
        override val type: BuildingType,
        override val children: List<PlaceDisplayable>,
        val extraChildren: List<PlaceDisplayable>,
    ) : Default(type, children)

    companion object {
        operator fun invoke(
            type: BuildingType,
            children: List<PlaceDisplayable>,
        ) = Value(type, children)

        operator fun invoke(
            type: BuildingType,
            children: List<PlaceDisplayable>,
            extraChildren: List<PlaceDisplayable>
        ) = ValueWithExtra(type, children, extraChildren)
    }
}

enum class BuildingType(val value: String, val category: BuildingCategory) {
    MAIN("본관", BuildingCategory.SCHOOL),
    NEWBUILDING("신관", BuildingCategory.SCHOOL),
    HAKBONG("학봉관", BuildingCategory.DORMITORY),
    UJEONG("우정학사", BuildingCategory.DORMITORY),
    ETC("기타 장소", BuildingCategory.ETC),
}

enum class BuildingCategory(val value: String) {
    SCHOOL("학교"),
    DORMITORY("생활관"),
    ETC("기타"),
}
