package common.db

import org.flywaydb.core.Flyway
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import javax.sql.DataSource

object DatabaseFactory {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun connectAndMigrate(jdbcUrl: String, migrationPath: String) {
        val pool = createConnectionPool(jdbcUrl)
        Database.connect(pool)
        runFlyway(pool, migrationPath)
    }

    private fun createConnectionPool(jdbcUrl: String): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            this.jdbcUrl = jdbcUrl
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    private fun runFlyway(datasource: DataSource, migrationPath: String) {
        val flyway = Flyway.configure()
            .dataSource(datasource)
            .locations(migrationPath)
            .load()
        try {
            flyway.info()
            flyway.migrate()
        } catch (e: Exception) {
            log.error("Exception running Flyway migration", e)
            throw e
        }
        log.info("Flyway migration has finished")
    }

    // Utilized in order to execute transactions in coroutine scope
    suspend fun <T> dbExec(
        block: () -> T
    ): T = withContext(Dispatchers.IO) {
        transaction { block() }
    }
}
