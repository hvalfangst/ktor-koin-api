package services

import models.User
import models.requests.UpsertUserRequest
import repositories.UserRepository

class UserService(private val userRepository: UserRepository) {

     suspend fun getUserById(id: Int): User? {
            return userRepository.getUserById(id)
    }

     suspend fun getAllUsers(): List<User> {
         return userRepository.getAllUsers()
    }

     suspend fun getUserByEmail(email: String): User? {
         return userRepository.getUserByEmail(email)
    }

     suspend fun createUser(request: UpsertUserRequest): User {
         return userRepository.createUser(request)
    }

     suspend fun updateUser(id: Int, request: UpsertUserRequest): User? {
         return userRepository.updateUser(id, request)
    }

     suspend fun deleteUser(id: Int): Any {
         return userRepository.deleteUser(id)
    }
}