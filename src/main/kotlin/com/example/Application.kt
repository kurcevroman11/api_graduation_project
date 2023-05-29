package com.example

import com.example.database.Role.RoleContriller
import com.example.database.user.UserContriller
import com.example.db.Task.TaskContriller
import com.example.db.Task.TaskDTO
import com.example.db.UserRoleProject.UserRoleProjectController
import com.example.features.login.configureLoginRouting
import com.example.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.github.cdimascio.dotenv.Dotenv
import mu.KotlinLogging
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import java.net.Socket

private val logger = KotlinLogging.logger {}

val dotenv = Dotenv.configure().load()

val host = dotenv["HOST"]
val port = dotenv["PORT"]
val db = dotenv["DB"]
val postgresNameUser = dotenv["POSTGRES_NAME_USER"]
val postgresPasswordUser = dotenv["POSTGRES_PASSWORD_USER"]

fun main() {

    waitForDatabase()

    logger.info { "Запускаем миграцию" }

    val flyway = Flyway.configure()
        .dataSource("jdbc:postgresql://$host:$port/$db", postgresNameUser, postgresPasswordUser)
    .baselineOnMigrate(true)
        .locations("db/migration") // указываем папку с миграциями
        .load()

    flyway.migrate()

    logger.info { "Хост равен $host" }

    Database.connect(
        url = "jdbc:postgresql://$host:$port/$db",
        driver = "org.postgresql.Driver",
        user = postgresNameUser.toString(),
        password = postgresPasswordUser.toString()
    )
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureLoginRouting()
    configureRegisterRouting()
    configureSerialization()
    SumNambers()
    configureRouting()
    login()
    TaskContriller()
    UserContriller()
    RoleContriller()
    UserRoleProjectController()
}

fun waitForDatabase() {
    val host = host
    val port = 5432

    while (true) {
        try {
            Socket(host, port).use { socket ->
                logger.info { "Порт базы данных доступен. Запуск приложения." }
                // Порт доступен, можно запускать приложение
                return
            }
        } catch (e: Exception) {
            logger.info { "Порт базы данных недоступен. Ожидание и повторная попытка через 1 секунду." }
            // Порт недоступен, ожидаем и повторяем попытку
            Thread.sleep(1000)
        }
    }
}
