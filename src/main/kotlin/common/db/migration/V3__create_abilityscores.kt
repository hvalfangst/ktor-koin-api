package common.db.migration

import models.tables.AbilitiesTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V3__create_abilityscores : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            createAbilityScoresTable()
        }
    }

    private fun createAbilityScoresTable() {
        SchemaUtils.create(AbilitiesTable)
    }
}
