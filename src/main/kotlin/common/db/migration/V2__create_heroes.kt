package common.db.migration

import models.tables.HeroesTable
import org.flywaydb.core.api.migration.BaseJavaMigration
import org.flywaydb.core.api.migration.Context
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class V2__create_heroes_table : BaseJavaMigration() {
    override fun migrate(context: Context?) {
        transaction {
            createHeroesTable()
        }
    }

    private fun createHeroesTable() {
        SchemaUtils.create(HeroesTable)
    }
}
