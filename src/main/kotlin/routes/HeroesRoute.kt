package routes

import common.messages.ErrorMessage
import common.security.AccessControl.Companion.validateAccess
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import models.Access
import models.Hero
import models.requests.UpsertHeroRequest
import org.koin.ktor.ext.inject
import services.HeroService

fun Route.heroesRoute() {
    val heroService: HeroService by inject()

    authenticate("auth-jwt") {
        route("/heroes") {

            get {
                validateAccess(call, Access.VIEWER)
                val heroes: List<Hero> = heroService.getAllHeroes()
                call.respond(heroes)
            }

            get("/{id}") {
                validateAccess(call, Access.VIEWER)
                val id = call.parameters["id"]!!.toInt()
                val hero: Hero? = heroService.getHeroById(id)

                when (hero != null) {
                    true -> call.respond(hero)
                    false -> call.respond(
                        ErrorMessage.HERO_NONEXISTENT.httpStatusCode,
                        ErrorMessage.HERO_NONEXISTENT.message
                    )

                }
            }


            post {
                validateAccess(call, Access.CREATOR)

                try {
                    val request = call.receive<UpsertHeroRequest>()
                    val createdHero = heroService.createHero(request)
                    if (createdHero != null) {
                        call.respond(createdHero)
                    } else {
                        call.respond(
                            ErrorMessage.HERO_CREATION_FAILED.httpStatusCode,
                            ErrorMessage.HERO_CREATION_FAILED.message
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
                            ErrorMessage.HERO_CREATION_FAILED.httpStatusCode,
                            ErrorMessage.HERO_CREATION_FAILED.message
                        )
                    }
                }
            }

            put("/{id}") {
                validateAccess(call, Access.EDITOR)
                val id = call.parameters["id"]!!.toInt()
                val request = call.receive<UpsertHeroRequest>()
                val updatedHero = heroService.updateHero(id, request)

                when (updatedHero != null) {
                    true -> call.respond(updatedHero)
                    false -> call.respond(
                        ErrorMessage.HERO_UPDATE_FAILED.httpStatusCode,
                        ErrorMessage.HERO_UPDATE_FAILED.message
                    )
                }
            }

            delete("/{id}") {
                validateAccess(call, Access.ADMIN)
                val id = call.parameters["id"]!!.toInt()

                when (heroService.deleteHero(id)) {
                    true -> call.respond("User with ID $id has been deleted")
                    false -> call.respond(
                        ErrorMessage.HERO_DELETION_FAILED.httpStatusCode,
                        ErrorMessage.HERO_DELETION_FAILED.message
                    )

                }
            }
        }
    }
}
