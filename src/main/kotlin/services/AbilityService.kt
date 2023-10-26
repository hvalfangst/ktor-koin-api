package services

import models.Ability
import models.requests.UpsertAbilityRequest
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repositories.AbilityRepository

class AbilityService: KoinComponent {

    private val abilityRepository: AbilityRepository by inject()

     suspend fun getAllAbilities(): List<Ability> {
        return abilityRepository.getAllAbilities()
    }

     suspend fun getAbilityById(id: Int): Ability? {
        return abilityRepository.getAbilityById(id)

    }

     suspend fun createAbility(request: UpsertAbilityRequest): Ability? {
        return abilityRepository.createAbility(request)
    }

     suspend fun updateAbility(id: Int, request: UpsertAbilityRequest): Ability? {
        return abilityRepository.updateAbility(id, request)

    }

     suspend fun deleteAbility(id: Int): Any {
        return abilityRepository.deleteAbility(id)

    }
}