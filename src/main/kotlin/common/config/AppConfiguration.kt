package common.config

import io.ktor.server.application.*

/**
 * Data class representing the application configuration.
 *
 * @property jdbcUrl The JDBC URL for the database.
 * @property issuer The issuer for JWT tokens.
 * @property secret The secret used for JWT token generation.
 * @property audience The intended audience for JWT tokens.
 * @property flywayMigrationPath The path used by Flyway for database migration scripts.
 */
data class AppConfiguration(
    val jdbcUrl: String,
    val issuer: String,
    val secret: String,
    val audience: String,
    val flywayMigrationPath: String
)

/**
 * Loads the application configuration from the Ktor application environment.
 *
 * @param environment The Ktor application environment.
 * @return An instance of [AppConfiguration] with the loaded configuration values.
 *
 * @throws IllegalArgumentException if any required configuration properties are missing.
 */
fun loadAppConfiguration(environment: ApplicationEnvironment): AppConfiguration {
    val jdbcUrl = requireNotNull(environment.config.propertyOrNull("db.jdbcUrl")?.getString()) { "db.jdbcUrl must be configured" }
    val issuer = requireNotNull(environment.config.propertyOrNull("jwt.issuer")?.getString()) { "jwt.issuer must be configured" }
    val secret = requireNotNull(environment.config.propertyOrNull("jwt.secret")?.getString()) { "jwt.secret must be configured" }
    val audience = requireNotNull(environment.config.propertyOrNull("jwt.audience")?.getString()) { "jwt.audience must be configured" }
    val flywayMigrationPath = requireNotNull(environment.config.propertyOrNull("flyway.migrationPath")?.getString()) { "flyway.migrationPath must be configured" }

    return AppConfiguration(jdbcUrl, issuer, secret, audience, flywayMigrationPath)
}
