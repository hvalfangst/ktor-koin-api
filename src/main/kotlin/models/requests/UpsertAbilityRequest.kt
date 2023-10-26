package models.requests

import kotlinx.serialization.Serializable

@Serializable
data class UpsertAbilityRequest(
    val heroName: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int
)
