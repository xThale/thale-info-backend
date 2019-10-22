package thale.info.auth

import java.time.OffsetDateTime

data class TokenInfo(
    val idToken: String,
    val refreshToken: String,
    val expiresAt: OffsetDateTime
)