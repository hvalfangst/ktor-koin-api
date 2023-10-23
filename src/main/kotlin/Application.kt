import common.config.initializeAppConfigSingleton
import common.security.JwtUtil
import common.db.DatabaseManager
import common.security.Hasher
import heroes.route.heroes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import users.repository.Repository as UsersRepo
import heroes.repository.Repository as HeroesRepo
import users.route.users

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    // Initialize global singleton for accessing environment variables derived from 'application.yml'
    initializeAppConfigSingleton(environment)

    // Connect to the database and run  flyway migrations
    DatabaseManager.connectAndMigrate()

    // Configure content negotiation to handle JSON
    install(ContentNegotiation) {
        json()
    }

    // Configure authentication middleware, which is used in endpoint 'login' for context 'users'
    install(Authentication) {
        basicAuthMiddleware()
    }

    // Configure routes '/users' and '/heroes'
    install(Routing) {
        users(JwtUtil(), UsersRepo())
        heroes(HeroesRepo())
    }
}

private fun AuthenticationConfig.basicAuthMiddleware() {
    basic("auth-basic") {
        validate { credentials ->
            val (username, password) = credentials
            val user = UsersRepo().getUserByEmail(username)
            if (user != null && Hasher.verify(password, user.password)) {
                UserIdPrincipal("$username|GUARDIAN|${user.role}")
            } else {
                null
            }
        }
    }
}
