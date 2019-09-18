package thale.info.security.model

import com.google.gson.annotations.SerializedName

/**
 * The response to a successful authentication of a google bearer token
 */
data class GoogleAuthenticationResponse(
    val email: String,
    @SerializedName("name") val firstName: String,
    val picture: String,
    @SerializedName("given_name") val lastName: String,
    val locale: String
) : AuthenticationResponse {
    override fun toAuthenticatedUser() = AuthenticatedUser(email, picture, firstName)
}