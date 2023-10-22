package users.route

import common.security.Hasher
import common.security.JwtUtil
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import common.messages.ErrorMessage
import io.ktor.server.auth.*
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

    authenticate("auth-basic") {
        route("/users/login") {
            post {
                val principal = call.principal<UserIdPrincipal>()
                if (principal != null) {
                    val (username, role) = principal.name.split(":")
                    val token = jwtUtil.generateToken(username, role)
                    call.respond(hashMapOf("token" to token))
                } else {
                    call.respond(
                        ErrorMessage.AUTH_MISSING_USER.httpStatusCode,
                        ErrorMessage.AUTH_MISSING_USER.message
                    )
                }
            }
        }
    }
}
