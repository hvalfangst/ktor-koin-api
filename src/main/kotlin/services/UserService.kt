package services

import common.db.DatabaseManager.executeInTransaction
import models.requests.UpsertUserRequest
import models.User
import repositories.UserRepository

class UserService(private val userRepository: UserRepository) {

    suspend fun getUserById(id: Int): User? {
        return executeInTransaction {
            userRepository.getUserById(id)
        }
    }

    suspend fun getAllUsers(): List<User> {
        return executeInTransaction {
            userRepository.getAllUsers()
        }
    }

    suspend fun getUserByEmail(email: String): User? {
        return executeInTransaction {
            userRepository.getUserByEmail(email)
        }
    }

    suspend fun createUser(request: UpsertUserRequest): User? {
        return executeInTransaction {
            userRepository.createUser(request)
        }
    }

    suspend fun updateUser(id: Int, request: UpsertUserRequest): User? {
        return executeInTransaction {
            userRepository.updateUser(id, request)
        }
    }

    suspend fun deleteUser(id: Int): Any {
        return executeInTransaction {
            userRepository.deleteUser(id)
        }
    }
}