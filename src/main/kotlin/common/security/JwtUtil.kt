package common.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import common.config.EnvironmentVariables
import org.koin.core.component.KoinComponent
import java.util.*
import org.koin.core.component.inject

class JwtUtil: KoinComponent {
    private val appConfig: EnvironmentVariables by inject()

    private val jwtVerifier: JWTVerifier = JWT.require(Algorithm.HMAC256(appConfig.secret))
        .withIssuer(appConfig.issuer)
        .build()

    fun generateToken(username: String, access: String): String {
        return JWT.create()
            .withAudience(appConfig.audience)
            .withIssuer(appConfig.issuer)
            .withClaim("username", username)
            .withClaim("access", access)
            .withExpiresAt(Date(System.currentTimeMillis() + (60000 * 5))) // 5 Minutes
            .sign(Algorithm.HMAC256(appConfig.secret))
    }

    @Throws(JWTVerificationException::class)
    fun verifyToken(token: String): DecodedJWT {
        return jwtVerifier.verify(token)
    }
}
