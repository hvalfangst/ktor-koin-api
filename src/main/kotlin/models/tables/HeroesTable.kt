package models.tables

import org.jetbrains.exposed.sql.Table

object HeroesTable : Table("heroes") {
    val id = integer("id").autoIncrement()
    val name = text("name").uniqueIndex()
    val level = integer("level")
    val hitPoints = integer("hit_points")
    val attack = integer("attack")
    val damage = integer("damage")
    val armorClass = integer("armor_class")

    override val primaryKey = PrimaryKey(id, name = "PK_Heroes_Id")
}