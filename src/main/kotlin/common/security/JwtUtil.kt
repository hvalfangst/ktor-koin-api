package common.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.DecodedJWT
import common.config.AppConfiguration
import java.util.*

class JwtUtil(private val appConfig: AppConfiguration) {
    private val jwtVerifier: JWTVerifier = JWT.require(Algorithm.HMAC256(appConfig.secret))
        .withIssuer(appConfig.issuer)
        .build()

    fun generateToken(username: String, role: String): String {
        return JWT.create()
            .withAudience(appConfig.audience)
            .withIssuer(appConfig.issuer)
            .withClaim("username", username)
            .withClaim("role", role)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000)) // 1 Minute
            .sign(Algorithm.HMAC256(appConfig.secret))
    }

    fun verifyToken(token: String): DecodedJWT {
        return jwtVerifier.verify(token)
    }
}
