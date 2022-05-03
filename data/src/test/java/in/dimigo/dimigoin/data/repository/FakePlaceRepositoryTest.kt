package `in`.dimigo.dimigoin.data.repository

import `in`.dimigo.dimigoin.data.repository.fake.FakePlaceRepository
import `in`.dimigo.dimigoin.domain.entity.place.AttendanceLog
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.core.test.TestCaseOrder
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class FakePlaceRepositoryTest : BehaviorSpec({

    given("FakePlaceRepository") {
        val repository = FakePlaceRepository()

        When("initiated") {
            and("call getPlaces") {
                val returns = repository.getPlaces().getOrThrow()
                then("it should return some places") {
                    returns.shouldNotBeEmpty()
                }
            }
            and("call getCurrentPlace") {
                val returns = repository.getCurrentPlace().getOrThrow()
                then("getCurrentPlace should return null") {
                    returns.shouldBeNull()
                }
            }
            and("call getBuildings") {
                val returns = repository.getBuildings().getOrThrow()
                then("getBuildings should return some buildings") {
                    returns.shouldNotBeNull()
                }
            }
            and("call getFavoriteAttendanceLogs") {
                val returns = repository.getFavoriteAttendanceLogs().getOrThrow()
                then("getFavoriteAttendanceLogs should return empty list") {
                    returns.shouldBeEmpty()
                }
            }
        }
        When("set current place to valid place") {
            val success = repository.setCurrentPlace("1234", null).getOrThrow()
            then("it should be successful") {
                success.shouldBeTrue()
            }
        }
        When("call currentPlace") {
            val currentPlace = repository.getCurrentPlace().getOrThrow()
            then("it should be the place of id 1234") {
                currentPlace.shouldNotBeNull()
                currentPlace._id shouldBe "1234"
            }
        }
        When("add favorite attendance log") {
            val log = AttendanceLog("1234", "집가고싶다")
            val success = repository.addFavoriteAttendanceLog(log).getOrThrow()
            then("it should be successful") {
                success.shouldBeTrue()
            }
        }
        When("call getFavoriteAttendanceLog") {
            val favoriteAttendanceLogs = repository.getFavoriteAttendanceLogs().getOrThrow()
            then("it should contain one element") {
                favoriteAttendanceLogs.size shouldBe 1
            }
        }
        When("remove first favorite attendance log") {
            val logBefore = repository.getFavoriteAttendanceLogs().getOrThrow()
            repository.removeFavoriteAttendanceLogById(logBefore.first()._id)

            and("call getFavoriteAttendanceLog") {
                val favoriteAttendanceLogs = repository.getFavoriteAttendanceLogs().getOrThrow()
                then("it should be empty") {
                    favoriteAttendanceLogs.shouldBeEmpty()
                }
            }
        }
    }
}) {
    override fun testCaseOrder(): TestCaseOrder = TestCaseOrder.Sequential
}