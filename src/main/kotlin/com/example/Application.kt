import com.example.database.Person.PersonContriller
import com.example.database.Role.RoleContriller
import com.example.database.Status.StatusContriller
import com.example.database.type_of_activity.Type_of_activityContriller
import com.example.database.user.UserContriller
import com.example.db.Description.DescriptionContriller
import com.example.db.Task.TaskContriller
import com.example.db.UserRoleProject.UserRoleProjectController
import com.example.features.login.configureLoginRouting
import com.example.features.register.configureRegisterRouting
import com.example.plugins.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mu.KotlinLogging
import org.jetbrains.exposed.sql.Database
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*


private val logger = KotlinLogging.logger {}

//val dotenv: Dotenv = Dotenv.configure().load()
//
//
//val host: String? = dotenv["HOST"]
//val port: String? = dotenv["PORT"]
//val postgresUser: String? = dotenv["POSTGRES_NAME_USER"]
//val postgresPassword: String? = dotenv["POSTGRES_PASSWORD_USER"]
//val dbName: String? = dotenv["DB"]

fun main() {
//
//    waitForDatabase()
//
//// настраиваем Flyway
//    val flyway = Flyway.configure()
//        .dataSource("jdbc:postgresql://$host:$port/$dbName", "$postgresUser", "$postgresPassword")
//        .baselineOnMigrate(true)
//        .locations("db/migration") // указываем папку с миграциями
//        .load()
//// запускаем миграции
//    flyway.migrate()

    // Запускаем БД
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/sebbia",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "qwerty"
    )

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
    .start(wait = true)
}

fun Application.module() {
    configureLoginRouting()
    configureRegisterRouting()
    configureSerialization()
    TaskContriller()
    UserContriller()
    RoleContriller()
    UserRoleProjectController()
    PersonContriller()
    Type_of_activityContriller()
    StatusContriller()
    DescriptionContriller()
    tokenUser()
    CommentContriller()
    configureRouting()
}

//fun waitForDatabase() {
//    val host: String? = dotenv["HOST"]
//    val port = 5432
//
//    while (true) {
//        try {
//            Socket(host, port).use { socket ->
//                logger.info { "Порт базы данных доступен. Запуск приложения." }
//                return
//            }
//        } catch (e: Exception) {
//            logger.info { "Порт базы данных недоступен. Ожидание и повторная попытка через 1 секунду." }
//            Thread.sleep(1000)
//        }
//    }
//}