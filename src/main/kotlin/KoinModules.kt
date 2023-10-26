import common.config.EnvironmentVariables
import common.db.DatabaseManager
import common.security.JwtUtil
import org.koin.dsl.module
import repositories.AbilityRepository
import repositories.HeroRepository
import repositories.UserRepository
import services.AbilityService
import services.HeroService
import services.UserService

val properties = module {
    single {
        EnvironmentVariables(
            getProperty("db.jdbcUrl", "NIL"),
            getProperty("jwt.issuer", "NIL"),
            getProperty("jwt.secret", "NIL"),
            getProperty("jwt.audience", "NIL"),
            getProperty("flyway.migrationPath", "NIL")
        )
    }
}

val db = module {
    single { DatabaseManager() }
}

val security = module {
    single { JwtUtil() }
}

val repositories = module {
    single { UserRepository() }
    single { HeroRepository() }
    single { AbilityRepository() }
}

val services = module {
    single { UserService() }
    single { HeroService() }
    single { AbilityService() }
}