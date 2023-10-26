package common.config

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routes.abilitiesRoute
import routes.heroesRoute
import routes.usersRoute

class RouteConfig {
    fun configure(application: Application) {
        application.install(Routing) {
            usersRoute()
            heroesRoute()
            abilitiesRoute()
        }
    }
}
