package com.example.features.login

import com.example.database.user.UsersDTO
import com.example.features.register.RegisterResponseRemote
import com.example.plugins.CartSession
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import io.ktor.util.*

data class User(val token: String)
fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val userAgent = call.request.header(HttpHeaders.UserAgent)
            println()
            println(userAgent)
            println()

            val loginController = LoginController(call)
            loginController.performLogin(userAgent)
        }
    }
}