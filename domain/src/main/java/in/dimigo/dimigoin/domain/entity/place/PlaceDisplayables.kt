package `in`.dimigo.dimigoin.domain.entity.place

interface PlaceDisplayable

sealed class PlaceCategory(
    open val description: String,
) : PlaceDisplayable {

    object None : PlaceCategory("")

    data class FloorCategory(
        val building: BuildingType,
        val floor: Floor,
        override val description: String,
    ) : PlaceCategory(description)

    data class NamedCategory(
        val name: String,
        override val description: String,
    ) : PlaceCategory(description)

    object MainPlaceCategoryProvider {
        val B1 = floor(BuildingType.MAIN, Floor.of(0), "급식실")
        val F1 = floor(BuildingType.MAIN, Floor.of(1), "교무실, 교실")
        val F2 = floor(BuildingType.MAIN, Floor.of(2), "교실, 특별실")
        val F3 = floor(BuildingType.MAIN, Floor.of(3), "동아리실, 방과후실")
    }

    object NewBuildingPlaceCategoryProvider {
        val F1 = floor(BuildingType.NEWBUILDING, Floor.of(1), "교무실, 특별실")
        val F2 = floor(BuildingType.NEWBUILDING, Floor.of(2), "교실, 특별실")
        val F3 = floor(BuildingType.NEWBUILDING, Floor.of(3), "열람실, 특별실")
        val F4 = floor(BuildingType.NEWBUILDING, Floor.of(4), "대강당")
    }

    companion object {
        val MAIN = MainPlaceCategoryProvider
        val NEW = NewBuildingPlaceCategoryProvider
        val ETC = named("기타 장소 및 사유", "저는 다른 곳에 있어요")

        fun floor(building: BuildingType, floor: Floor, description: String) =
            FloorCategory(building, floor, description)

        fun named(name: String, description: String) =
            NamedCategory(name, description)

        fun from(building: BuildingType, floor: Floor): PlaceCategory =
            when (building) {
                BuildingType.MAIN -> when (floor) {
                    is Floor.BaseValue -> MAIN.B1
                    Floor.Ground -> None
                    Floor.None -> None
                    Floor.Underground -> None
                    is Floor.Value -> when (floor.value) {
                        1 -> MAIN.F1
                        2 -> MAIN.F2
                        3 -> MAIN.F3
                        else -> None
                    }
                }
                BuildingType.NEWBUILDING -> when (floor) {
                    is Floor.Value -> when (floor.value) {
                        1 -> NEW.F1
                        2 -> NEW.F2
                        3 -> NEW.F3
                        4 -> NEW.F4
                        else -> None
                    }
                    else -> None
                }
                else -> None
            }
    }
}

data class Place(
    val _id: String,
    val name: String,
    val alias: String,
    val description: String,
    val placeCategory: PlaceCategory,
    val type: PlaceType,
) : PlaceDisplayable

enum class PlaceType(val value: String) {
    CLASSROOM("교실"),
    RESTROOM("화장실"),
    CIRCLE("동아리실"),
    AFTERSCHOOL("방과후실"),
    TEACHER("교무실"),
    CORRIDOR("복도"),
    ETC("기타"),
    FARM("스마트팜"),
    PLAYGROUND("운동장"),
    GYM("체육관"),
    LAUNDRY("세탁"),
    ABSENT("결석"),
    SCHOOL("학교 건물"),
    DORMITORY("기숙사 건물")
}

sealed class Floor {
    object None : Floor()
    object Ground : Floor() {
        override fun toString(): String = "지상"
    }

    object Underground : Floor() {
        override fun toString(): String = "지하"
    }

    class Value(val value: Int) : Floor() {
        override fun toString(): String = "${value}층"
    }

    class BaseValue(val value: Int) : Floor() {
        override fun toString(): String = "B${value}층"
    }

    companion object {
        fun of(floor: Int) =
            if (floor < 1) {
                BaseValue(1 - floor)
            } else {
                Value(floor)
            }

        fun none() = None
        fun ground() = Ground
        fun underground() = Underground
    }
}
