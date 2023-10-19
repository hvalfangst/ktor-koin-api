import db.DatabaseFactory
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    val jdbcUrl = environment.config.propertyOrNull("db.jdbcUrl")?.getString() ?: "NIL"
    DatabaseFactory.connectAndMigrate(jdbcUrl)
}
