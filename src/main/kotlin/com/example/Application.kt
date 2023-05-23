package com.example

import com.example.database.user.UserContriller
import com.example.db.Task.TaskContriller
import com.example.db.Task.TaskDTO
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

//    val task: TaskDTO = fetchTask(2)
//    println(task.name)
//    val date = task.start_date!!.toDate()
//    println(date)
//    val date2 = task.start_date.toLocalDateTime()
//    println(date2)
//
//    val data2_1 =  task.start_date.toLocalDateTime().toString()
//    println(data2_1)
//
//    val date3 = date2.hourOfDay
//    println(date3)


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
}
