package `in`.dimigo.dimigoin.data.mapper

import `in`.dimigo.dimigoin.data.model.meal.MealResponseModel
import `in`.dimigo.dimigoin.data.model.place.PlaceResponseModel
import `in`.dimigo.dimigoin.data.model.user.IdentityResponseModel
import `in`.dimigo.dimigoin.domain.entity.meal.Meal
import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import `in`.dimigo.dimigoin.domain.entity.place.Place
import `in`.dimigo.dimigoin.domain.entity.place.PlaceType
import `in`.dimigo.dimigoin.domain.entity.schedule.Schedule
import `in`.dimigo.dimigoin.domain.entity.schedule.ScheduleType
import `in`.dimigo.dimigoin.domain.entity.user.User
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDate

class MapperTest : BehaviorSpec({
    given("AttendanceLog") {
        val originalLog = AttendanceLog("1234", "동아리 활동", "9cc0f71a-97d6-4961-8b1b-36c3e0c2beee")
        When("Convert to json") {
            val json = originalLog.toJsonString()
            and("Revert back to AttendanceLog") {
                val revertedLog = json.toAttendanceLog()
                then("it should be equal to the original AttendanceLog") {
                    revertedLog shouldBe originalLog
                }
            }
        }
    }

    given("MealResponseModel") {
        val model = MealResponseModel(
            listOf("쌀밥", "된장국", "과일", "우유", "선식", "샌드위치"),
            listOf("현미밥", "김치찌개", "김치", "투움바파스타", "레몬에이드"),
            listOf("차조밥", "캠핑구이", "스프", "샐러드"),
            date = "2022-02-02",
        )
        When("Convert to Meal entity") {
            val entity = model.toEntity()
            then("it should return converted Meal entity") {
                val expected = Meal(
                    "쌀밥 | 된장국 | 과일 | 우유 | 선식 | 샌드위치",
                    "현미밥 | 김치찌개 | 김치 | 투움바파스타 | 레몬에이드",
                    "차조밥 | 캠핑구이 | 스프 | 샐러드",
                    LocalDate.of(2022, 2, 2)
                )

                entity shouldBe expected
            }
        }
    }

    given("PlaceResponseModel") {
        val model = PlaceResponseModel("123123", PlaceType.CORRIDOR, "본관 2층 복도", null, "MAIN", 2)
        When("Convert to Place entity") {
            val entity = model.toEntity()
            then("it should return converted Place entity") {
                val expected =
                    Place("123123", "본관 2층 복도", "본관 2층 복도", "복도", "본관", "2층", PlaceType.CORRIDOR)

                entity shouldBe expected
            }
        }
    }

    mapOf(
        LocalDateRange(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31)) to 30,
        LocalDateRange(LocalDate.of(2022, 7, 3), LocalDate.of(2022, 7, 10)) to 7,
        LocalDateRange(LocalDate.of(2022, 12, 25), LocalDate.of(2022, 12, 26)) to 1,
    ).forEach { (range, expected) ->
        given(range.toString()) {
            When("LocalDateRange.repeatScheduleInDateRange") {
                val size = repeatScheduleInDateRange(ScheduleType.EVENT, "전체귀가", range).size
                then("it should have size of $expected") {
                    size shouldBe expected
                }
            }
        }
    }

    given("Schedule") {
        val originalEntity = Schedule(ScheduleType.EVENT, LocalDate.of(2022, 3, 2), "개학식")
        When("Convert to LocalScheduleModel") {
            val model = originalEntity.toLocalModel()
            and("Revert back to Schedule entity") {
                val revertedEntity = model.toEntity()
                then("it should be equal to the original Schedule entity") {
                    revertedEntity shouldBe originalEntity
                }
            }
        }
    }

    given("IdentityResponseModel") {
        val model = IdentityResponseModel(
            "홍길동",
            1, 7, 33,
            1733,
            listOf("https://example.com/photo1", "https://example.com/photo2"),
            "2000-01-01",
            "1234567890",
            listOf(),
        )
        When("Convert to User entity") {
            val entity = model.toEntity()
            then("it should return converted User entity") {
                val expected = User(
                    "홍길동",
                    1, 7, 33,
                    1733,
                    listOf("https://example.com/photo1", "https://example.com/photo2"),
                    LocalDate.of(2000, 1, 1),
                    "1234567890",
                    listOf(),
                )

                entity shouldBe expected
            }
        }
    }
})
