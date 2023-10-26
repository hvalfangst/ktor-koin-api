package repositories

import common.db.executeInTransaction
import models.Ability
import models.requests.UpsertAbilityRequest
import models.tables.AbilitiesTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AbilityRepository {

    suspend fun getAllAbilities(): List<Ability> {
        return executeInTransaction {
            AbilitiesTable.selectAll().map { toAbility(it) }
        }
    }

    suspend fun getAbilityById(id: Int): Ability? {
        return executeInTransaction {
            AbilitiesTable
                .select { AbilitiesTable.id eq id }
                .map { toAbility(it) }
                .singleOrNull()
        }
    }

    suspend fun getAbilityByHeroName(heroName: String): Ability? {
        return executeInTransaction {
            AbilitiesTable.select { AbilitiesTable.heroName eq heroName }
                .map { toAbility(it) }
                .singleOrNull()
        }
    }

    suspend fun createAbility(request: UpsertAbilityRequest): Ability? {
        return executeInTransaction {
            var createdAbilityId: Int? = null

            createdAbilityId = AbilitiesTable.insert {
                it[heroName] = request.heroName
                it[strength] = request.strength
                it[dexterity] = request.dexterity
                it[constitution] = request.constitution
                it[intelligence] = request.intelligence
                it[wisdom] = request.wisdom
                it[charisma] = request.charisma
            } get AbilitiesTable.id

            createAbilityResponse(createdAbilityId, request)
        }
    }

    suspend fun updateAbility(id: Int, request: UpsertAbilityRequest): Ability? {
        return executeInTransaction {
            AbilitiesTable.update({ AbilitiesTable.id eq id }) {
                it[heroName] = request.heroName
                it[strength] = request.strength
                it[dexterity] = request.dexterity
                it[constitution] = request.constitution
                it[intelligence] = request.intelligence
                it[wisdom] = request.wisdom
                it[charisma] = request.charisma
            }

            createAbilityResponse(id, request)
        }
    }

    suspend fun deleteAbility(id: Int): Boolean {
        return executeInTransaction {
            val deletedRows = AbilitiesTable.deleteWhere { AbilitiesTable.id eq id }
            deletedRows > 0
        }
    }

    private fun createAbilityResponse(id: Int, request: UpsertAbilityRequest) = Ability(
        id = id,
        heroName = request.heroName,
        strength = request.strength,
        dexterity = request.dexterity,
        constitution = request.constitution,
        intelligence = request.intelligence,
        wisdom = request.wisdom,
        charisma = request.charisma
    )

    private fun toAbility(row: ResultRow): Ability =
        Ability(
            id = row[AbilitiesTable.id],
            heroName = row[AbilitiesTable.heroName],
            strength = row[AbilitiesTable.strength],
            dexterity = row[AbilitiesTable.dexterity],
            constitution = row[AbilitiesTable.constitution],
            intelligence = row[AbilitiesTable.intelligence],
            wisdom = row[AbilitiesTable.wisdom],
            charisma = row[AbilitiesTable.charisma]
        )
}