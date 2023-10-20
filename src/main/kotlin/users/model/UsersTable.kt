package users.model

import org.jetbrains.exposed.sql.Table

object UsersTable : Table() {
    val id = integer("id").autoIncrement()
    val email = text("email").uniqueIndex()
    val password = text("password")
    val fullname = text("fullname")
    val role = text("role")

    override val primaryKey = PrimaryKey(id, name = "PK_Users_Id")
}