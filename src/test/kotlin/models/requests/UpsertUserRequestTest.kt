package models.requests

import io.mockk.every
import io.mockk.mockk
import models.Access
import org.junit.Test
import kotlin.test.assertFailsWith


class UpsertUserRequestTest {

    @Test
    fun `Valid full name and email should not throw an exception`() {
        // Arrange
        val validFullName = "Mister Pastor"
        val validEmail = "mrpastor77@rebukemail.com"
        val access = Access.VIEWER

        UpsertUserRequest(validEmail, "password", validFullName, access)
    }

    @Test
    fun `Invalid full name should throw an exception`() {
        // Arrange
        val invalidFullName = "MrPastor77" // Contains two digits

        // Act and Assert
        assertFailsWith<IllegalArgumentException> {
            UpsertUserRequest("mrpastor77@rebuke.com", "password", invalidFullName, Access.VIEWER)
        }
    }

    @Test
    fun `Invalid email should throw an exception`() {
        // Arrange
        val invalidEmail = "DecreeAndDeclare" // Missing "@" symbol

        // Act and Assert
        assertFailsWith<IllegalArgumentException> {
            UpsertUserRequest(invalidEmail, "password", "Mister Pastor", Access.VIEWER)
        }
    }

    @Test
    fun `Mocking validation functions`() {
        // Arrange
        val request = UpsertUserRequest("mister.pastor77@rebukemail.com", "password", "Mister Pastor", Access.VIEWER)

        // Mock the validation functions
        val isValidFullNameMock = mockk<(String) -> Boolean>()
        val isValidEmailMock = mockk<(String) -> Boolean>()

        every { isValidFullNameMock(any()) } returns true
        every { isValidEmailMock(any()) } returns true

        // Act
        val isValid = isValidFullNameMock(request.fullName) && isValidEmailMock(request.email)

        // Assert
        assert(isValid)
    }
}
