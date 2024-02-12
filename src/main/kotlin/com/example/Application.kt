package com.example

import CommentContriller
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
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.net.Socket

private val logger = KotlinLogging.logger {}

val dotenv: Dotenv = Dotenv.configure().load()


val host: String? = dotenv["HOST"]
val port: String? = dotenv["PORT"]
val postgresUser: String? = dotenv["POSTGRES_NAME_USER"]
val postgresPassword: String? = dotenv["POSTGRES_PASSWORD_USER"]
val dbName: String? = dotenv["DB"]

fun main() {

    waitForDatabase()

// настраиваем Flyway
    val flyway = Flyway.configure()
        .dataSource("jdbc:postgresql://$host:$port/$dbName", "$postgresUser", "$postgresPassword")
        .baselineOnMigrate(true)
        .locations("db/migration") // указываем папку с миграциями
        .load()
    //Обновление истории схем
    flyway.repair()
    // запускаем миграции
    flyway.migrate()

    // Запускаем БД
    Database.connect(
        url = "jdbc:postgresql://$host:$port/$dbName",
        driver = "org.postgresql.Driver",
        user = "$postgresUser",
        password = "$postgresPassword"
    )

    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureCORS()
    configureSerialization()
    configureLoginRouting()
    configureRegisterRouting()
    TaskContriller()
    UserContriller()
    RoleContriller()
    UserRoleProjectController()
    PersonContriller()
    Type_of_activityContriller()
    StatusContriller()
    DescriptionContriller()
    CommentContriller()
    tokenUser()
    header()
    cookie()
    heades()
}

fun waitForDatabase() {
    while (true) {
        try {
            Socket(host, port!!.toInt()).use { socket ->
                logger.info { "Порт базы данных доступен. Запуск приложения." }
                return
            }
        } catch (e: Exception) {
            logger.info { "Порт базы данных недоступен. Ожидание и повторная попытка через 1 секунду." }
            Thread.sleep(1000)
        }
    }
}