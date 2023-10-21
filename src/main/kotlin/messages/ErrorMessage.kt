package messages

import io.ktor.http.*

enum class ErrorMessage(val message: String, val httpStatusCode: HttpStatusCode) {
    USER_ALREADY_EXISTS("A user associated with the given email address already exists in the system", HttpStatusCode.BadRequest),
    NONEXISTENT_USER("The requested user does not exist", HttpStatusCode.NotFound),
    USER_CREATION_FAILED("Failed to create user", HttpStatusCode.BadRequest),
    USER_UPDATE_FAILED("Failed to update user", HttpStatusCode.BadRequest),
    USER_DELETION_FAILED("Failed to delete user", HttpStatusCode.BadRequest);
}
