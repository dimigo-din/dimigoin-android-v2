package `in`.dimigo.dimigoin.data.repository

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class FakeUserRepositoryTest : BehaviorSpec({
    given("FakeUserRepository") {
        val repository = FakeUserRepository()
        When("login with incorrect credential") {
            val success = repository.login("idunno", "mypassword").getOrThrow()
            then("it should be failure") {
                success.shouldBeFalse()
            }
        }
        When("login with correct credential") {
            val success = repository.login("test_account", "password!!").getOrThrow()
            then("it should be successful") {
                success.shouldBeTrue()
            }
        }
    }
})