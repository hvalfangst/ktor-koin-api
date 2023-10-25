package models.requests

import kotlinx.serialization.Serializable
import models.Role

@Serializable
data class UpsertUserRequest(
    val email: String,
    val password: String,
    val fullName: String,
    val role: Role
) {
    init {
        require(isValidEmail(email)) { "Invalid email address format" }
        require(isValidFullName(fullName)) { "Invalid fullname format" }
    }

    /**
    * Validates a full name to ensure it contains only letters and spaces and enforces a maximum length.
    *
    * @param fullname The full name to be validated.
    * @return `true` if the full name is valid; `false` otherwise.
    */
    private fun isValidFullName(fullname: String): Boolean {
        val fullnameRegex = Regex("^[A-Za-z ]{1,64}\$")
        return fullname.matches(fullnameRegex)
    }

    /** Regular expression to validate email addresses:
       ------------------------------------------------------------------------------
         ^[A-Za-z] - Start with any upper or lower-case letter
         (.*) - Match up to 64 characters before the '@' symbol
         [@] - Match exactly one '@' symbol
        (.+) - Match one or more characters after the '@' symbol (the domain part)
        \\. - Match exactly one dot (.) after the domain part
        (.{1,3}) - Match one to three characters after the dot (TLD - Top-Level Domain)
    -----------------------------------------------------------------------------------
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*){1,64}([@])(.+){1,255}(\\.)(.{1,3})")
        return email.matches(emailRegex)
    }
}