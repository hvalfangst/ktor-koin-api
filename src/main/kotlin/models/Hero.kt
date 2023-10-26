package models

import kotlinx.serialization.Serializable

@Serializable
data class Hero(
    val id: Int,
    val name: String,
    val hitPoints: Int,
    val attack: Int,
    val damage: Int,
    val armorClass: Int,
    val level: Int
)