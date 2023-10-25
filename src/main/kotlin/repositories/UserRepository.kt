package repositories

import common.security.Hasher
import models.requests.UpsertUserRequest
import models.User
import models.tables.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {

    fun getAllUsers(): List<User> {
        return UsersTable.selectAll().map { toUser(it) }

    }

    fun getUserById(id: Int): User? {
        return UsersTable.select { UsersTable.id eq id }
            .map { toUser(it) }
            .singleOrNull()

    }

    fun getUserByEmail(email: String): User? {
        return UsersTable.select { UsersTable.email eq email }
            .map { toUser(it) }
            .singleOrNull()

    }

    fun createUser(request: UpsertUserRequest): User? {
        var createdUserId: Int? = null
        val hashedPassword = Hasher.hash(request.password)


        createdUserId = UsersTable.insert {
            it[email] = request.email
            it[password] = hashedPassword
            it[fullname] = request.fullName
            it[role] = request.role.roleName
        } get UsersTable.id


        return createdUserId?.let { getUserById(it) }
    }

    fun updateUser(id: Int, request: UpsertUserRequest): User? {

        UsersTable.update({ UsersTable.id eq id }) {
            it[email] = request.email
            it[password] = Hasher.hash(request.password)
            it[fullname] = request.fullName
            it[role] = request.role.roleName
        }

        return getUserById(id)
    }

    fun deleteUser(id: Int): Boolean {
        val deletedRows = UsersTable.deleteWhere { UsersTable.id eq id }
        return deletedRows > 0
    }

    private fun toUser(row: ResultRow): User =
        User(
            id = row[UsersTable.id],
            email = row[UsersTable.email],
            password = row[UsersTable.password],
            fullname = row[UsersTable.fullname],
            role = row[UsersTable.role]
        )
}
