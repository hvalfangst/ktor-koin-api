import db.DatabaseFactory
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
    val jdbcUrl = environment.config.propertyOrNull("db.jdbcUrl")?.getString() ?: "NIL"
    DatabaseFactory.connectAndMigrate(jdbcUrl)

    val usersRepository = usersRepo()

    install(ContentNegotiation) {
        json()
    }

    install(Routing) {
        users(usersRepository)
    }
}
