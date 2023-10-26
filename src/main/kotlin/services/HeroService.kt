package services

import models.Hero
import models.requests.UpsertHeroRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repositories.HeroRepository

class HeroService: KoinComponent {

    private val heroRepository: HeroRepository by inject()

    suspend fun createHero(request: UpsertHeroRequest): Hero? {
        return heroRepository.createHeroResponse(request)
    }

    suspend fun getAllHeroes(): List<Hero> {
        return heroRepository.getAllHeroes()
    }

    suspend fun getHeroById(id: Int): Hero? {
        return heroRepository.getHeroById(id)
    }

    suspend fun updateHero(id: Int, request: UpsertHeroRequest): Hero? {
        return heroRepository.updateHero(id, request)
    }

    suspend fun deleteHero(id: Int): Any {
        return heroRepository.deleteHero(id)
    }
}