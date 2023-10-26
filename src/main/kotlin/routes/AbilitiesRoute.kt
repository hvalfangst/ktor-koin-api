package routes

import common.messages.ErrorMessage
import common.security.AccessControl.Companion.validateAccess
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Ability
import models.Access
import models.requests.UpsertAbilityRequest
import org.koin.ktor.ext.inject
import services.AbilityService

fun Route.abilitiesRoute() {
    val abilityService: AbilityService by inject()

    authenticate("auth-jwt") {
        route("/abilities") {

            get {
                validateAccess(call, Access.VIEWER)
                val abilities: List<Ability> = abilityService.getAllAbilities()
                call.respond(abilities)
            }

            get("/{id}") {
                validateAccess(call, Access.VIEWER)
                val id = call.parameters["id"]!!.toInt()
                val Ability: Ability? = abilityService.getAbilityById(id)

                when (Ability != null) {
                    true -> call.respond(Ability)
                    false -> call.respond(
                        ErrorMessage.ABILITY_NONEXISTENT.httpStatusCode,
                        ErrorMessage.ABILITY_NONEXISTENT.message
                    )

                }
            }


            post {
                validateAccess(call, Access.CREATOR)

                try {
                    val request = call.receive<UpsertAbilityRequest>()
                    val createdAbility = abilityService.createAbility(request)
                    if (createdAbility != null) {
                        call.respond(createdAbility)
                    } else {
                        call.respond(
                            ErrorMessage.ABILITY_CREATION_FAILED.httpStatusCode,
                            ErrorMessage.ABILITY_CREATION_FAILED.message
                        )
                    }
                } catch (e: Exception) {
                    if (e is BadRequestException) {
                        e.printStackTrace()
                        call.respond(
                            ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.httpStatusCode,
                            ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.message
                        )
                    } else {
                        e.printStackTrace()
                        call.respond(
                            ErrorMessage.ABILITY_CREATION_FAILED.httpStatusCode,
                            ErrorMessage.ABILITY_CREATION_FAILED.message
                        )
                    }
                }
            }

            put("/{id}") {
                validateAccess(call , Access.EDITOR)
                val id = call.parameters["id"]!!.toInt()
                val request = call.receive<UpsertAbilityRequest>()
                val updatedAbility = abilityService.updateAbility(id, request)

                when (updatedAbility != null) {
                    true -> call.respond(updatedAbility)
                    false -> call.respond(
                        ErrorMessage.ABILITY_UPDATE_FAILED.httpStatusCode,
                        ErrorMessage.ABILITY_UPDATE_FAILED.message
                    )
                }
            }

            delete("/{id}") {
                validateAccess(call,Access.ADMIN)
                val id = call.parameters["id"]!!.toInt()

                when (abilityService.deleteAbility(id)) {
                    true -> call.respond("Ability with ID $id has been deleted")
                    false -> call.respond(
                        ErrorMessage.ABILITY_DELETION_FAILED.httpStatusCode,
                        ErrorMessage.ABILITY_DELETION_FAILED.message
                    )

                }
            }
        }
    }
}

