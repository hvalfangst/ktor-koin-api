package common.config

import common.db.DatabaseManager
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*

class AppConfig {
    fun configure(application: Application) {

        // Connect to the database and run Flyway migrations
        DatabaseManager().connectAndMigrate()

        // Configure content negotiation to handle JSON
        application.install(ContentNegotiation) {
            json()
        }

    }
}
