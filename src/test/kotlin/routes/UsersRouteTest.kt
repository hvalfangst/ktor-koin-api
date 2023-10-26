package routes

import common.security.JwtUtil
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import models.Access
import models.User
import org.junit.Test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeEach
import services.UserService

class UsersRouteTest {
    private val userService = mockk<UserService>()
    private val jwtUtil = mockk<JwtUtil>()

    @BeforeEach
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `Get all users should return a list of users`() = testApplication {

        val users = listOf(
            User(
                1,
                "ernst@gmail.com",
                "kokemakk",
                "Ernst Van Haeckl",
                Access.VIEWER.toString,
            )
        )

        every {
            runBlocking { userService.getAllUsers()  }
        } returns users


        val response = client.get("/users")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, world!", response.bodyAsText())
    }

    // Test other route handlers in a similar way

    @AfterAll
    fun cleanup() {
        unmockkAll()
    }
}
