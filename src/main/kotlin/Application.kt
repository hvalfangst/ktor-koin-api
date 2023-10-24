import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.Payload
import common.config.initializeAppConfigSingleton
import common.security.JwtUtil
import common.db.DatabaseManager
import common.security.Hasher
import heroes.route.heroes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import users.repository.Repository as UsersRepo
import heroes.repository.Repository as HeroesRepo
import users.route.users

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
        users(jwtUtil, UsersRepo())
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

private fun AuthenticationConfig.jwtAuthMiddleware(jwtUtil: JwtUtil) {
    bearer("auth-jwt") {
        authenticate { credential ->
            val decodedJWT = jwtUtil.verifyToken(credential.token)
            val usernameClaim = decodedJWT.getClaim("username")
            val roleClaim = decodedJWT.getClaim("role")

            if (!usernameClaim.isNull && !roleClaim.isNull) {
                val username = usernameClaim.asString()
                val role = roleClaim.asString()

                // Log token details and checks
                println("Decoded Token: $decodedJWT")
                println("Username: $username, Role: $role")

                // Check if the user has the required role (if specified)
                if (role == "ADMIN") {
                    UserIdPrincipal("$username|GUARDIAN|$role")
                } else {
                    null
                }
            } else {
                null
            }
        }
    }
}

