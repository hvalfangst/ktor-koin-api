package common.config

import io.ktor.server.application.*

/**
 * Singleton object for managing and providing global access to the application configuration.
 *
 * The `AppConfig` object serves as a global singleton for storing and providing access to
 * the application configuration, represented by an instance of [AppConfiguration]. By setting
 * the `instance` property of this object, the application configuration can be accessed
 * from anywhere within the application without the need to pass it as a parameter.
 */
object AppConfig {
    lateinit var instance: AppConfiguration
}

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
 * derived from 'application.yaml' and sets them in the AppConfig singleton for global accessibility.
 *
 * This function loads configuration values, such as database connection details and JWT settings,
 * from the provided Ktor application environment. It then creates an instance of [AppConfiguration] and
 * sets it in the global [AppConfig] singleton, making the configuration accessible throughout the application.
 *
 * @param environment The Ktor application environment containing configuration properties.
 *
 * @throws IllegalArgumentException if any of the required configuration properties are missing.
 */
fun initializeAppConfigSingleton(environment: ApplicationEnvironment) {
    val config = AppConfiguration(
        environment.config.propertyOrNull("db.jdbcUrl")?.getString() ?: error("db.jdbcUrl must be configured"),
        environment.config.propertyOrNull("jwt.issuer")?.getString() ?: error("jwt.issuer must be configured"),
        environment.config.propertyOrNull("jwt.secret")?.getString() ?: error("jwt.secret must be configured"),
        environment.config.propertyOrNull("jwt.audience")?.getString() ?: error("jwt.audience must be configured"),
        environment.config.propertyOrNull("flyway.migrationPath")?.getString() ?: error("flyway.migrationPath must be configured")
    )
    AppConfig.instance = config
}

