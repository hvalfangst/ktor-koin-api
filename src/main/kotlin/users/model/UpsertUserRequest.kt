import kotlinx.serialization.Serializable

@Serializable
data class UpsertUserRequest(
    val email: String,
    val password: String,
    val fullname: String,
    val role: String
) {
    init {
        require(isValidEmail(email)) { "Invalid email address format" }
    }

    /** Regular expression to validate email addresses:
       ------------------------------------------------------------------------------
         ^[A-Za-z] - Start with a letter (case insensitive)
         (.*) - Match any characters (zero or more) before the '@' symbol
         [@]{1} - Match exactly one '@' symbol
        (.{1,}) - Match one or more characters after the '@' symbol (the domain part)
        \\. - Match a dot (.) after the domain part
        (.{1,}) - Match one or more characters after the dot (TLD - Top-Level Domain)
    -----------------------------------------------------------------------------------
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        return email.matches(emailRegex)
    }
}
