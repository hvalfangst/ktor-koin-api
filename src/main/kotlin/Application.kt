import common.config.initializeAppConfigSingleton
import common.db.DatabaseManager
import common.security.AccessControl.Companion.DELIMITER
import common.security.Hasher
import common.security.JwtUtil
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import repositories.AbilityRepository
import repositories.HeroRepository
import repositories.UserRepository
import routes.abilitiesRoute
import routes.heroesRoute
import routes.usersRoute
import services.AbilityService
import services.HeroService
import services.UserService

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {


    // Initialize singleton for accessing environment variables derived from 'application.yml'
    val configSingleton = initializeAppConfigSingleton(environment)

    // Connect to the database and run  flyway migrations
    DatabaseManager.connectAndMigrate(configSingleton)

    // Configure content negotiation to handle JSON
    install(ContentNegotiation) {
        json()
    }

    // Configure JWT utility class
    val jwtUtil = JwtUtil(configSingleton)

    // Configure authentication middleware, which is used in endpoint 'login' for context 'users'
    install(Authentication) {
        basicAuthMiddleware()
        jwtAuthMiddleware(jwtUtil)
    }

    // Configure routes '/users' and '/heroes'
    install(Routing) {
        usersRoute(jwtUtil, UserService(UserRepository()))
        heroesRoute(HeroService(HeroRepository()))
        abilitiesRoute(AbilityService(AbilityRepository()))
    }
}

private fun AuthenticationConfig.basicAuthMiddleware() {
    basic("auth-basic") {
        validate { credentials ->
            val (username, password) = credentials
            val user = UserRepository().getUserByEmail(username)
            if (user != null && Hasher.verify(password, user.password)) {
                UserIdPrincipal("$username${DELIMITER}${user.access}")
            } else {
                null
            }
        }
    }
}

// TODO: Consider adapting this to Handler instead for call context on exceptions
private fun AuthenticationConfig.jwtAuthMiddleware(jwtUtil: JwtUtil) {
    bearer("auth-jwt") {
        authenticate { credential ->
            val decodedJWT = jwtUtil.verifyToken(credential.token)
            val usernameClaim = decodedJWT.getClaim("username")
            val accessClaim = decodedJWT.getClaim("access")

            if (!usernameClaim.isNull && !accessClaim.isNull) {
                val access = accessClaim.asString()
                UserIdPrincipal(access)
            } else {
                null
            }
        }
    }
}

