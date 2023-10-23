package common.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import common.config.AppConfig
import java.util.*

class JwtUtil() {
    fun generateToken(username: String, role: String): String {
        return JWT.create()
            .withAudience(AppConfig.instance.audience)
            .withIssuer(AppConfig.instance.issuer)
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000)) // 1 Minute
            .sign(Algorithm.HMAC256(AppConfig.instance.secret))
    }
}
