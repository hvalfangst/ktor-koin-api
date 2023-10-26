package repositories

import common.db.executeInTransaction
import models.Hero
import models.tables.HeroesTable
import models.requests.UpsertHeroRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class HeroRepository {

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

    suspend fun createHeroResponse(request: UpsertHeroRequest): Hero {
         return executeInTransaction {
             var createdHeroId: Int? = null

             createdHeroId = HeroesTable.insert {
                 it[name] = request.name
                 it[hitPoints] = request.hitPoints
                 it[attack] = request.attack
                 it[damage] = request.damage
                 it[armorClass] = request.armorClass
                 it[level] = request.level
             } get HeroesTable.id
             createHeroResponse(createdHeroId, request)
         }
    }


    suspend fun updateHero(id: Int, request: UpsertHeroRequest): Hero? {
        return executeInTransaction {
            HeroesTable.update({ HeroesTable.id eq id }) {
                it[name] = request.name
                it[hitPoints] = request.hitPoints
                it[attack] = request.attack
                it[damage] = request.damage
                it[armorClass] = request.armorClass
                it[level] = request.level
            }
            createHeroResponse(id, request)
        }
    }

    suspend fun deleteHero(id: Int): Boolean {
        return executeInTransaction {
            val deletedRows = HeroesTable.deleteWhere { HeroesTable.id eq id }
            deletedRows > 0
        }
    }

    private fun createHeroResponse(createdHeroId: Int, request: UpsertHeroRequest) = Hero(
        id = createdHeroId,
        name = request.name,
        hitPoints = request.hitPoints,
        attack = request.attack,
        damage = request.damage,
        armorClass = request.armorClass,
        level = request.level
    )

    private fun toHero(row: ResultRow): Hero =
        Hero(
            id = row[HeroesTable.id],
            name = row[HeroesTable.name],
            hitPoints = row[HeroesTable.hitPoints],
            attack = row[HeroesTable.attack],
            damage = row[HeroesTable.damage],
            armorClass = row[HeroesTable.armorClass],
            level = row[HeroesTable.level]
        )
}
