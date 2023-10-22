package common.messages

import io.ktor.http.*

/**
 * Enum representing common error messages and their associated HTTP status codes.
 */
enum class ErrorMessage(val message: String, val httpStatusCode: HttpStatusCode) {

    // User related errors
    USER_ALREADY_EXISTS("A user associated with the given email address already exists in the system", HttpStatusCode.BadRequest),
    USER_NONEXISTENT("The requested user does not exist", HttpStatusCode.NotFound),
    USER_CREATION_FAILED("Failed to create user", HttpStatusCode.BadRequest),
    USER_UPDATE_FAILED("Failed to update user", HttpStatusCode.BadRequest),
    USER_DELETION_FAILED("Failed to delete user", HttpStatusCode.BadRequest),

    // Request body error
    REQUEST_BODY_VALIDATION_FAILURE("Validation of request body fields failed", HttpStatusCode.BadRequest),

    // Authentication and authorization related errors
    AUTH_INVALID_HEADER("Missing or malformed authorization header detected", HttpStatusCode.Unauthorized),
    AUTH_MISSING_USER("Failed to identify any users matching mail associated with 'username' in Basic header", HttpStatusCode.Unauthorized),
    AUTH_PASSWORD_MISMATCH("Password mismatch between header content and database detected", HttpStatusCode.Unauthorized)
}
