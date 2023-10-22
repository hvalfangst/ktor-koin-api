import common.config.loadAppConfiguration
import common.security.JwtUtil
import common.db.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import users.repository.Repository as UsersRepo
import users.route.users

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val appConfig = loadAppConfiguration(environment)

    // Connect to the database and run migrations
    DatabaseFactory.connectAndMigrate(appConfig.jdbcUrl, appConfig.flywayMigrationPath)

    // Configure content negotiation to handle JSON
    install(ContentNegotiation) {
        json()
    }

    // Define the 'users' route and configure it with JWT authentication and the user repository
    install(Routing) {
        users(JwtUtil(appConfig), UsersRepo())
    }
}
