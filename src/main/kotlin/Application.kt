import common.config.initializeAppConfigSingleton
import common.security.JwtUtil
import common.db.DatabaseManager
import common.security.Hasher
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import users.repository.Repository as UsersRepo
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

    // Define the 'users' route and configure it with JWT authentication and the user repository
    install(Routing) {
        users(JwtUtil(), UsersRepo())
    }
}
