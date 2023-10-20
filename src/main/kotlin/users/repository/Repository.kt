package users.repository

import common.security.Hasher
import db.DatabaseFactory.dbExec
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import users.model.UpsertUserRequest
import users.model.User
import users.model.UsersTable

class Repository {

    suspend fun getAllUsers(): List<User> {
        return dbExec {
            UsersTable.selectAll().map { toUser(it) }
        }
    }

    suspend fun getUser(id: Int): User? {
        return dbExec {
            UsersTable.select { UsersTable.id eq id }
                .map { toUser(it) }
                .singleOrNull()
        }
    }

    suspend fun createUser(request: UpsertUserRequest): User? {
        var createdUserId: Int? = null
        dbExec {
            createdUserId = UsersTable.insert {
                it[email] = request.email
                it[password] = Hasher.hash(request.password)
                it[fullname] = request.fullname
                it[role] = request.role
            } get UsersTable.id
        }

        // Retrieve the created user from the database
        return createdUserId?.let { getUser(it) }
    }

    suspend fun updateUser(id: Int, request: UpsertUserRequest): User? {
        dbExec {
            UsersTable.update({ UsersTable.id eq id }) {
                it[email] = request.email
                it[password] = Hasher.hash(request.password)
                it[fullname] = request.fullname
                it[role] = request.role
            }
        }
        return getUser(id)
    }

    suspend fun deleteUser(id: Int): Boolean = dbExec {
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
