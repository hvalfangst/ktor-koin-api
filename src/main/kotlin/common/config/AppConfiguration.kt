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
 * Initializes the application configuration by loading values from the Ktor application environment
 * derived from 'application.yaml' and returns an instance of [AppConfiguration].
 *
 * This function loads configuration values, such as database connection details and JWT settings,
 * from the provided Ktor application environment.
 *
 * @param environment The Ktor application environment containing configuration properties.
 * @return An instance of [AppConfiguration] with the loaded configuration values.
 * @throws IllegalArgumentException if any of the required configuration properties are missing.
 */
    fun initializeAppConfigSingleton(environment: ApplicationEnvironment) : AppConfiguration {
    return AppConfiguration(
        environment.config.propertyOrNull("db.jdbcUrl")?.getString() ?: error("db.jdbcUrl must be configured"),
        environment.config.propertyOrNull("jwt.issuer")?.getString() ?: error("jwt.issuer must be configured"),
        environment.config.propertyOrNull("jwt.secret")?.getString() ?: error("jwt.secret must be configured"),
        environment.config.propertyOrNull("jwt.audience")?.getString() ?: error("jwt.audience must be configured"),
        environment.config.propertyOrNull("flyway.migrationPath")?.getString() ?: error("flyway.migrationPath must be configured")
    )
}

