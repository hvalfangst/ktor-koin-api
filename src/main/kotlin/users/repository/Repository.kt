package users.repository

import users.model.UpsertUserRequest
import common.db.DatabaseManager.executeInTransaction
import common.security.Hasher
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import users.model.User
import users.model.UsersTable

class Repository {

    suspend fun getAllUsers(): List<User> {
        return executeInTransaction {
            UsersTable.selectAll().map { toUser(it) }
        }
    }

    suspend fun getUserById(id: Int): User? {
        return executeInTransaction {
            UsersTable.select { UsersTable.id eq id }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return executeInTransaction {
            UsersTable.select { UsersTable.email eq email }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    suspend fun createUser(request: UpsertUserRequest): User? {
        var createdUserId: Int? = null
        val hashedPassword = Hasher.hash(request.password)

        executeInTransaction {
            createdUserId = UsersTable.insert {
                it[email] = request.email
                it[password] = hashedPassword
                it[fullname] = request.fullname
                it[role] = request.role.roleName
            } get UsersTable.id
        }

        return createdUserId?.let { getUserById(it) }
    }

    suspend fun updateUser(id: Int, request: UpsertUserRequest): User? {
        executeInTransaction {
            UsersTable.update({ UsersTable.id eq id }) {
                it[email] = request.email
                it[password] = Hasher.hash(request.password)
                it[fullname] = request.fullname
                it[role] = request.role.roleName
            }
        }
        return getUserById(id)
    }

    suspend fun deleteUser(id: Int): Boolean = executeInTransaction {
        val deletedRows = UsersTable.deleteWhere { UsersTable.id eq id }
        deletedRows > 0
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
