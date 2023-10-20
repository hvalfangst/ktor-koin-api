package users.model

import kotlinx.serialization.Serializable

@Serializable
data class UpsertUserRequest(
    val email: String,
    val password: String,
    val fullname: String,
    val role: String
)