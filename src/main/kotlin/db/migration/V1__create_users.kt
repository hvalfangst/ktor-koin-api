package db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction

object Users : Table() {
    val id = integer("id").autoIncrement()
    val email = text("email")
    val password = text("password")
    val fullname = text("fullname")
    val role = text("role")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")
}

class V1__create_users_table : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            createUsersTable()
        }
    }

    private fun createUsersTable() {
        SchemaUtils.create(Users)
    }
}
