package heroes.repository

import common.db.DatabaseManager.executeInTransaction
import heroes.model.Hero
import heroes.model.HeroesTable
import heroes.model.UpsertHeroRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class Repository {

    suspend fun getAllHeroes(): List<Hero> {
        return executeInTransaction {
            HeroesTable.selectAll().map { toHero(it) }
        }
    }

    suspend fun getHeroById(id: Int): Hero? {
        return executeInTransaction {
            HeroesTable.select { HeroesTable.id eq id }
                .map { toHero(it) }
                .singleOrNull()
        }
    }

    suspend fun getHeroByName(name: String): Hero? {
        return executeInTransaction {
            HeroesTable.select { HeroesTable.name eq name }
                .map { toHero(it) }
                .singleOrNull()
        }
    }

    suspend fun createHero(request: UpsertHeroRequest): Hero? {
        var createdHeroId: Int? = null

        executeInTransaction {
            createdHeroId = HeroesTable.insert {
                it[name] = request.name
                it[hitPoints] = request.hitPoints
                it[attack] = request.attack
                it[damage] = request.damage
                it[armorClass] = request.armorClass
            } get HeroesTable.id
        }

        return createdHeroId?.let { getHeroById(it) }
    }

    suspend fun updateHero(id: Int, request: UpsertHeroRequest): Hero? {
        executeInTransaction {
            HeroesTable.update({ HeroesTable.id eq id }) {
                it[name] = request.name
                it[hitPoints] = request.hitPoints
                it[attack] = request.attack
                it[damage] = request.damage
                it[armorClass] = request.armorClass
            }
        }
        return getHeroById(id)
    }

    suspend fun deleteHero(id: Int): Boolean = executeInTransaction {
        val deletedRows = HeroesTable.deleteWhere { HeroesTable.id eq id }
        deletedRows > 0
    }

    private fun toHero(row: ResultRow): Hero =
        Hero(
            id = row[HeroesTable.id],
            name = row[HeroesTable.name],
            hitPoints = row[HeroesTable.hitPoints],
            attack = row[HeroesTable.attack],
            damage = row[HeroesTable.damage],
            armorClass = row[HeroesTable.armorClass]

        )
}
