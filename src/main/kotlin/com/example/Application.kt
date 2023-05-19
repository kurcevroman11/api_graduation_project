package com.example

import com.example.db.dataDb.password
import com.example.db.dataDb.url
import com.example.db.dataDb.user
import com.example.features.login.configureLoginRouting
import com.example.features.register.configureRegisterRouting
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database


fun main() {
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/Sebbia",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "123321"
    )

   embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)


// настраиваем Flyway
    val flyway = Flyway.configure()
        .dataSource(url, user, password)
        .baselineOnMigrate(true)
        .locations("db/migration") // указываем папку с миграциями
        .load()

// запускаем миграции
    flyway.migrate()
}

fun Application.module() {
    configureLoginRouting()
    configureSerialization()
    CreaytUser()
    SumNambers()
    configureRouting()
    filetext()
    photo()
    configureRegisterRouting()
}
