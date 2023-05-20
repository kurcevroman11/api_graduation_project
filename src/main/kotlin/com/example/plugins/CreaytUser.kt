package com.example.plugins

import com.example.db.dbHelper
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.SQLException


fun Application.CreaytUser() {



    // Определяем маршрут для POST запроса на добавление нового пользователя
    routing {
        post("/users") {
            // Получаем данные пользователя из тела запроса
            val json = call.receive<String>()

            val gson = Gson()

            val name = gson.fromJson(json,User::class.java)

            // Сохраняем пользователя в базе данных
            try {

                var dbHelper = dbHelper()
                dbHelper.creatyTable(name.name)

                // Отправляем ответ с данными пользователя
                call.respond(name.toString())
            } catch (e: SQLException) {
                // Если произошла ошибка при выполнении запроса, возвращаем ошибку
                call.respond(HttpStatusCode.InternalServerError, "Error adding user")
            }
        }
    }
}




data class User(val name: String)
