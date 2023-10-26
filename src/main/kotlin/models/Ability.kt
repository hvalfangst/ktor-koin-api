package models

@kotlinx.serialization.Serializable
data class Ability(
    val id: Int,
    val heroName: String,
    val strength: Int,
    val dexterity: Int,
    val constitution: Int,
    val intelligence: Int,
    val wisdom: Int,
    val charisma: Int
)