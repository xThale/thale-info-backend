package thale.info.api.security.authentication.model

import java.time.OffsetDateTime

data class TokenExchangeResponse(
    val idToken: String,
    val refreshToken: String,
    val expiresAt: OffsetDateTime
)