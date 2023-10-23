package heroes.model

import kotlinx.serialization.Serializable

@Serializable
data class UpsertHeroRequest(
    val name: String,
    val hitPoints: Int,
    val attack: Int,
    val damage: Int,
    val armorClass: Int
)