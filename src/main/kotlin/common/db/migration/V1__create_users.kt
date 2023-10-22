package common.db.migration

import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import users.model.UsersTable

class V1__create_users_table : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            createUsersTable()
        }
    }

    private fun createUsersTable() {
        SchemaUtils.create(UsersTable)
    }
}
