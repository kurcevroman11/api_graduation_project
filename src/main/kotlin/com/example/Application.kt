package com.example

import com.example.features.login.configureLoginRouting
import com.example.features.register.configureRegisterRouting
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

fun main() {

// настраиваем Flyway
    val flyway = Flyway.configure()
        .dataSource("jdbc:postgresql://localhost:5432/sebbia", "postgres", "qwerty")
        .baselineOnMigrate(true)
        .locations("db/migration") // указываем папку с миграциями
        .load()
// запускаем миграции
    flyway.migrate()

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
//    SumNambers()
//    configureRouting()
//    login()
}
