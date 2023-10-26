package common.config

import common.security.AccessControl
import common.security.Hasher
import common.security.JwtUtil
import io.ktor.server.application.*
import io.ktor.server.auth.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import repositories.UserRepository

/**
 * Configuration class for setting up authentication in the Ktor application.
 */
class AuthConfig: KoinComponent {
    private val jwtUtil: JwtUtil by inject()


    /**
     * Configure authentication methods within the Ktor application.
     *
     * @param application The Ktor application instance.
     */
    fun configure(application: Application) {

        // Install the Authentication feature and configure authentication methods
        application.install(Authentication) {

            // Configure basic authentication
            basic("auth-basic") {
                validate { credentials ->
                    val (username, password) = credentials
                    val user = UserRepository().getUserByEmail(username)
                    if (user != null && Hasher.verify(password, user.password)) {
                        // Return the concatenation of username and access if authentication is successful
                        UserIdPrincipal("$username${AccessControl.DELIMITER}${user.access}")
                    } else {
                        null
                    }
                }
            }

            // Configure JWT bearer authentication
            bearer("auth-jwt") {
                authenticate { credential ->
                    val decodedJWT = jwtUtil.verifyToken(credential.token)
                    val usernameClaim = decodedJWT.getClaim("username")
                    val accessClaim = decodedJWT.getClaim("access")

                    if (!usernameClaim.isNull && !accessClaim.isNull) {
                        // Return the access on success
                        val access = accessClaim.asString()
                        UserIdPrincipal(access)
                    } else {
                        null
                    }
                }
            }
        }
    }
}
