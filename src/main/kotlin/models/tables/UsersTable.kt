package models.tables

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val id = integer("id").autoIncrement()
    val email = text("email").uniqueIndex()
    val password = text("password")
    val fullname = text("fullname")
    val access = text("access")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")
}