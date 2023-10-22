package users.route

import common.security.Hasher
import common.security.JwtUtil
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import common.messages.ErrorMessage
import users.model.UpsertUserRequest
import users.model.User
import users.repository.Repository
import java.util.*

fun Route.users(jwtUtil: JwtUtil, userRepository: Repository) {
    route("/users") {

        get {
            val users: List<User> = userRepository.getAllUsers()
            call.respond(users)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val user: User? = userRepository.getUserById(id)

            when (user != null) {
                true -> call.respond(user)
                false -> call.respond(
                    ErrorMessage.USER_NONEXISTENT.httpStatusCode,
                    ErrorMessage.USER_NONEXISTENT.message
                )

            }
        }

        post {
            try {
                val request = call.receive<UpsertUserRequest>()
                val existingUser = userRepository.getUserByEmail(request.email)

                if (existingUser != null) {
                    call.respond(
                        ErrorMessage.USER_ALREADY_EXISTS.httpStatusCode,
                        ErrorMessage.USER_ALREADY_EXISTS.message
                    )
                } else {
                    val createdUser = userRepository.createUser(request)
                    if (createdUser != null) {
                        call.respond(createdUser)
                    } else {
                        call.respond(
                            ErrorMessage.USER_CREATION_FAILED.httpStatusCode,
                            ErrorMessage.USER_CREATION_FAILED.message
                        )
                    }
                }
            } catch (e: Exception) {
                call.respond(
                    ErrorMessage.USER_CREATION_FAILED.httpStatusCode,
                    ErrorMessage.USER_CREATION_FAILED.message
                )
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val request = call.receive<UpsertUserRequest>()
            val updatedUser = userRepository.updateUser(id, request)

            when (updatedUser != null) {
                true -> call.respond(updatedUser)
                false -> call.respond(
                    ErrorMessage.USER_UPDATE_FAILED.httpStatusCode,
                    ErrorMessage.USER_UPDATE_FAILED.message
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]!!.toInt()

            when (userRepository.deleteUser(id)) {
                true -> call.respond("User with ID $id has been deleted")
                false -> call.respond(
                    ErrorMessage.USER_DELETION_FAILED.httpStatusCode,
                    ErrorMessage.USER_DELETION_FAILED.message
                )

            }
        }
    }

    route("/users/login") {
        post {
            val authorizationHeader = call.request.headers["Authorization"]
            if (authorizationHeader != null && authorizationHeader.startsWith("Basic ")) {
                val credentials = extractAndDecodeHeader(authorizationHeader)
                val (username, password) = credentials.split(":")

                val user = userRepository.getUserByEmail(username)

                if(user == null) {
                    call.respond(
                        ErrorMessage.AUTH_MISSING_USER.httpStatusCode,
                        ErrorMessage.AUTH_MISSING_USER.message
                    )
                } else {
                     if (!Hasher.verify(password, user.password)) {
                         call.respond(
                             ErrorMessage.AUTH_PASSWORD_MISMATCH.httpStatusCode,
                             ErrorMessage.AUTH_PASSWORD_MISMATCH.message
                         )
                     } else{
                         val token = jwtUtil.generateToken(user.email, user.role)
                         call.respond(hashMapOf("token" to token))
                     }
                }
            } else {
                call.respond(
                    ErrorMessage.AUTH_INVALID_HEADER.httpStatusCode,
                    ErrorMessage.AUTH_INVALID_HEADER.message
                )
            }
        }
    }
}

/**
 *  Extract and decode the base64-encoded credentials
 */
private fun extractAndDecodeHeader(authorizationHeader: String): String {
    val base64Credentials = authorizationHeader.removePrefix("Basic ").trim()
    return String(Base64.getDecoder().decode(base64Credentials))
}
