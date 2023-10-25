package services

import common.db.DatabaseManager.executeInTransaction
import models.Hero
import models.requests.UpsertHeroRequest
import repositories.HeroRepository

class HeroService(private val heroRepository: HeroRepository) {

    suspend fun createHero(request: UpsertHeroRequest): Hero? {
        return executeInTransaction {
            heroRepository.createHero(request)
        }
    }

    suspend fun getAllHeroes(): List<Hero> {
        return executeInTransaction {
            heroRepository.getAllHeroes()
        }

    }

    suspend fun getHeroById(id: Int): Hero? {
        return executeInTransaction {
            heroRepository.getHeroById(id)
        }
    }

    suspend fun updateHero(id: Int, request: UpsertHeroRequest): Hero? {
        return executeInTransaction {
            heroRepository.updateHero(id, request)
        }
    }

    suspend fun deleteHero(id: Int): Any {
        return executeInTransaction {
            heroRepository.deleteHero(id)
        }
    }
}