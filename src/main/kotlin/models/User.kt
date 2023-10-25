package models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val email: String,
    val password: String,
    val fullname: String,
    val role: String
)