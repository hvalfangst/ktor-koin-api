import common.config.loadAppConfiguration
import common.security.JwtUtil
import common.db.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import users.repository.Repository as usersRepo
import users.route.users

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(ContentNegotiation) {
        json()
    }

    val appConfig = loadAppConfiguration(environment)
    val jwtUtil = JwtUtil(appConfig)
    DatabaseFactory.connectAndMigrate(appConfig.jdbcUrl, appConfig.flywayMigrationPath)
    val usersRepository = usersRepo()



    install(Routing) {
        users(jwtUtil, usersRepository)
    }
}
