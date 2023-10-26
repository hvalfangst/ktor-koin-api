import common.config.AppConfig
import common.config.AuthConfig
import common.config.RouteConfig
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.logger.PrintLogger
import org.koin.fileProperties
import org.koin.core.logger.Level

fun main() {
    embeddedServer(Netty, port = 8080, module = Application::module).start(wait = true)
}

fun Application.module() {

    startKoin {
        logger(PrintLogger(Level.INFO))
        modules(properties, db, security, repositories, services)
        fileProperties("/koin.properties")
    }

    AppConfig().configure(this)
    AuthConfig().configure(this)
    RouteConfig().configure(this)
}