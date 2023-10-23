package users.model

enum class Role(val roleName: String) {
    VIEWER( "VIEWER"),
    CREATOR("CREATOR"),
    EDITOR("EDITOR"),
    ADMIN("ADMIN")
}