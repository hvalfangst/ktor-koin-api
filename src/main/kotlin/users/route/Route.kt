package users.route

import NONEXISTENT_USER
import USERCREATION_FAILED
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import users.model.UpsertUserRequest
import users.model.User
import users.repository.Repository

fun Route.users(userRepository: Repository) {
    route("/users") {

            get {
                val users: List<User> = userRepository.getAllUsers()
                call.respond(users)
            }

            get("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val user: User? = userRepository.getUser(id)

                when (user != null) {
                    true -> call.respond(user)
                    false -> {
                        call.respond(HttpStatusCode.NotFound, NONEXISTENT_USER)
                    }
                }
            }

        post {
                val request = call.receive<UpsertUserRequest>()
                val createdUser = userRepository.createUser(request)

                when (createdUser != null) {
                    true ->  call.respond(createdUser)
                    false -> {
                        call.respond(HttpStatusCode.InternalServerError, USERCREATION_FAILED)
                    }
                }
            }

            put("/{id}") {
                val id = call.parameters["id"]!!.toInt()
                val request = call.receive<UpsertUserRequest>()
                val updatedUser = userRepository.updateUser(id, request)

                when (updatedUser != null) {
                    true -> call.respond(updatedUser)
                    false -> {
                        call.respond(HttpStatusCode.NotFound, NONEXISTENT_USER)
                    }
                }
            }

            delete("/{id}") {
                val id = call.parameters["id"]!!.toInt()

                when (userRepository.deleteUser(id)) {
                    true -> call.respond("User with ID $id has been deleted")
                    false -> {
                        call.respond(HttpStatusCode.NotFound, NONEXISTENT_USER)
                    }
                }
            }
        }
}
