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
            val hashedPasswordBytes = BCrypt.withDefaults().hash(COST, string.toByteArray());
            return Base64.getEncoder().encodeToString(hashedPasswordBytes)
        }
    }
}

