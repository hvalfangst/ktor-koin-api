package models.tables

import org.jetbrains.exposed.sql.Table

object AbilitiesTable : Table("abilities") {
    val id = integer("id").autoIncrement()
    val strength = integer("strength")
    val dexterity = integer("dexterity")
    val constitution = integer("constitution")
    val intelligence = integer("intelligence")
    val wisdom = integer("wisdom")
    val charisma = integer("charisma")

    val heroName = reference("hero_name", HeroesTable.name)
    override val primaryKey = PrimaryKey(id, name = "PK_Abilities_Id")

    init {
        index(true, heroName)
    }
}
