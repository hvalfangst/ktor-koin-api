package heroes.route

import common.messages.ErrorMessage
import heroes.model.Hero
import heroes.model.UpsertHeroRequest
import heroes.repository.Repository
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.heroes(heroRepository: Repository) {
    route("/heroes") {

        get {
            val heroes: List<Hero> = heroRepository.getAllHeroes()
            call.respond(heroes)
        }

        get("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val hero: Hero? = heroRepository.getHeroById(id)

            when (hero != null) {
                true -> call.respond(hero)
                false -> call.respond(
                    ErrorMessage.HERO_NONEXISTENT.httpStatusCode,
                    ErrorMessage.HERO_NONEXISTENT.message
                )

            }
        }

        authenticate("auth-jwt") {
            post {
                try {
                    val request = call.receive<UpsertHeroRequest>()
                    val createdHero = heroRepository.createHero(request)
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
                        call.respond(ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.httpStatusCode, ErrorMessage.REQUEST_BODY_VALIDATION_FAILURE.message)
                    } else {
                        e.printStackTrace()
                        call.respond(ErrorMessage.HERO_CREATION_FAILED.httpStatusCode, ErrorMessage.HERO_CREATION_FAILED.message)
                    }
                }
            }
        }

        put("/{id}") {
            val id = call.parameters["id"]!!.toInt()
            val request = call.receive<UpsertHeroRequest>()
            val updatedHero = heroRepository.updateHero(id, request)

            when (updatedHero != null) {
                true -> call.respond(updatedHero)
                false -> call.respond(
                    ErrorMessage.HERO_UPDATE_FAILED.httpStatusCode,
                    ErrorMessage.HERO_UPDATE_FAILED.message
                )
            }
        }

        delete("/{id}") {
            val id = call.parameters["id"]!!.toInt()

            when (heroRepository.deleteHero(id)) {
                true -> call.respond("User with ID $id has been deleted")
                false -> call.respond(
                    ErrorMessage.HERO_DELETION_FAILED.httpStatusCode,
                    ErrorMessage.HERO_DELETION_FAILED.message
                )

            }
        }
    }
}
