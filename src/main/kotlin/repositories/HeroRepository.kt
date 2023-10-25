package repositories

import models.Hero
import models.tables.HeroesTable
import models.requests.UpsertHeroRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class HeroRepository {

     fun getAllHeroes(): List<Hero> {
        return HeroesTable.selectAll().map { toHero(it) }
    }

     fun getHeroById(id: Int): Hero? {
        return HeroesTable.select { HeroesTable.id eq id }
                .map { toHero(it) }
                .singleOrNull()
    }

     fun getHeroByName(name: String): Hero? {
        return HeroesTable.select { HeroesTable.name eq name }
                .map { toHero(it) }
                .singleOrNull()
    }

     fun createHero(request: UpsertHeroRequest): Hero? {
        var createdHeroId: Int? = null

            createdHeroId = HeroesTable.insert {
                it[name] = request.name
                it[hitPoints] = request.hitPoints
                it[attack] = request.attack
                it[damage] = request.damage
                it[armorClass] = request.armorClass
            } get HeroesTable.id

        return createdHeroId?.let { getHeroById(it) }
    }

     fun updateHero(id: Int, request: UpsertHeroRequest): Hero? {
            HeroesTable.update({ HeroesTable.id eq id }) {
                it[name] = request.name
                it[hitPoints] = request.hitPoints
                it[attack] = request.attack
                it[damage] = request.damage
                it[armorClass] = request.armorClass
            }
        return getHeroById(id)
    }

    fun deleteHero(id: Int): Boolean {
        val deletedRows = HeroesTable.deleteWhere { HeroesTable.id eq id }
        return deletedRows > 0
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
