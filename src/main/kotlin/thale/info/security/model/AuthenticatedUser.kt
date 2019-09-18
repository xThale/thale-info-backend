package thale.info.security.model

/**
 * User who's token was successfully authenticated
 */
data class AuthenticatedUser(
    val email: String,
    val avatar: String,
    val name: String
)