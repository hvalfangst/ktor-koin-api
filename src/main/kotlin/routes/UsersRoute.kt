package routes

import models.requests.UpsertUserRequest
import common.messages.ErrorMessage
import common.security.JwtUtil
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.User
import services.UserService

fun Route.usersRoute(jwtUtil: JwtUtil, userService: UserService) {
    route("/users") {

        get {
            val users: List<User> = userService.getAllUsers()
            call.respond(users)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val user: User? = userService.getUserById(id)

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
                val existingUser = userService.getUserByEmail(request.email)

                if (existingUser != null) {
                    call.respond(
                        ErrorMessage.USER_ALREADY_EXISTS.httpStatusCode,
                        ErrorMessage.USER_ALREADY_EXISTS.message
                    )
                } else {
                    val createdUser = userService.createUser(request)
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
                if (e is BadRequestException) {
                    e.printStackTrace()
                    call.respond(ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.httpStatusCode, ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.message)
                } else {
                    e.printStackTrace()
                    call.respond(ErrorMessage.USER_CREATION_FAILED.httpStatusCode, ErrorMessage.USER_CREATION_FAILED.message)
                }
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val request = call.receive<UpsertUserRequest>()
            val updatedUser = userService.updateUser(id, request)

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

            when (userService.deleteUser(id)) {
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
                    val (username, role) = principal.name.split("|GUARDIAN|")
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
