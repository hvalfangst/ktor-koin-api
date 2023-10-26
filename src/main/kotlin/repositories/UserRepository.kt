package repositories

import common.db.DatabaseManager.executeInTransaction
import common.security.Hasher
import models.User
import models.requests.UpsertUserRequest
import models.tables.UsersTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class UserRepository {

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

    suspend fun createUser(request: UpsertUserRequest): User {
        return executeInTransaction {
            val createdUserId = UsersTable.insert {
                it[email] = request.email
                it[password] = Hasher.hash(request.password)
                it[fullname] = request.fullName
                it[access] = request.access.toString()
            }

            createUserResponse(createdUserId.insertedCount, request)
        }
    }

    /**
     * TODO: Validate this
     */
    suspend fun updateUser(id: Int, request: UpsertUserRequest): User? {
        return executeInTransaction {
            UsersTable.update({ UsersTable.id eq id }) {
                it[password] = Hasher.hash(request.password)
                it[fullname] = request.fullName
                it[access] = request.access.toString
            }

            createUserResponse(id, request)
        }
    }

    suspend fun deleteUser(id: Int): Boolean {
        return executeInTransaction {
            val deletedRows = UsersTable.deleteWhere { UsersTable.id eq UsersTable.id }
            deletedRows > 0
        }
    }

    private fun createUserResponse(id: Int, request: UpsertUserRequest) = User(
        id = id,
        email = request.email,
        password = Hasher.hash(request.password),
        fullName = request.fullName,
        access = request.access.toString
    )

    private fun toUser(row: ResultRow): User =
        User(
            id = row[UsersTable.id],
            email = row[UsersTable.email],
            password = row[UsersTable.password],
            fullName = row[UsersTable.fullname],
            access = row[UsersTable.access]
        )
}
