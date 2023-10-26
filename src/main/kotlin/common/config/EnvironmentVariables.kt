package common.config


/**
 * Data class representing the application configuration.
 *
 * @property jdbcUrl The JDBC URL for the database.
 * @property issuer The issuer for JWT tokens.
 * @property secret The secret used for JWT token generation.
 * @property audience The intended audience for JWT tokens.
 * @property flywayMigrationPath The path used by Flyway for database migration scripts.
 */
data class EnvironmentVariables(
    val jdbcUrl: String,
    val issuer: String,
    val secret: String,
    val audience: String,
    val flywayMigrationPath: String
) {
    init {
        require(jdbcUrl != "NIL") { "jdbcUrl cannot have the value 'NIL'" }
        require(issuer != "NIL") { "issuer cannot have the value 'NIL'" }
        require(secret != "NIL") { "secret cannot have the value 'NIL'" }
        require(audience != "NIL") { "audience cannot have the value 'NIL'" }
        require(flywayMigrationPath != "NIL") { "flywayMigrationPath cannot have the value 'NIL'" }
    }
}

