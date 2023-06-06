package com.example.database.user

import com.example.database.user.UserModule.deleteUser
import com.example.database.user.UserModule.fetchAllUser
import com.example.database.user.UserModule.fetchUserID
import com.example.database.user.UserModule.updateUser
import io.ktor.server.application.*
import io.ktor.server.routing.*
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.UserContriller() {
    routing {
        route("/User") {
            //Вывод всех пользователей
            get {
                val usersDTO = fetchAllUser()
                call.response.header(HttpHeaders.AccessControlAllowOrigin, "http://localhost:3000")

                call.respond(usersDTO)
            }
            //Вывод определенного пользователя
            get("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = fetchUserID(userId)
                    call.respond(user!!)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
            //Обновление определенного пользователя
            put("/{id}") {

                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    val user = call.receive<String>()
                    val gson = Gson()

                    val userDTO = gson.fromJson(user, UsersDTO::class.java)
                    call.respond(updateUser(userId, userDTO))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
            //Удаление определенного пользователя
            delete("/{id}") {
                val userId = call.parameters["id"]?.toIntOrNull()
                if (userId != null) {
                    call.respond(deleteUser(userId), "Delete")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
        }
    }
}