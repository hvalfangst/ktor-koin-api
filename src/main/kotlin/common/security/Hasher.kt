package common.security

import at.favre.lib.crypto.bcrypt.BCrypt
import java.util.*

class Hasher {
    companion object {
        private const val COST = 12;
        /**
         *  Hashes a string with Bcrypt and base64 encodes the associated resulting ByteArray
         */
        fun hash(string: String): String {
            val startTime = System.currentTimeMillis()
            val hashedPasswordBytes = BCrypt.withDefaults().hash(COST, string.toByteArray())
            val base64EncodedPassword = Base64.getEncoder().encodeToString(hashedPasswordBytes)
            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime
            println("Hashing and encoding took $elapsedTime milliseconds")
            return base64EncodedPassword
        }
    }
}

