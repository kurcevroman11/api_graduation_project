package com.example.features.login

import com.example.database.user.UsersDTO
import com.example.features.register.RegisterResponseRemote
import com.example.plugins.CartSession
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val userAgent = call.request.header(HttpHeaders.UserAgent)
            val loginController = LoginController(call)
            loginController.performLogin(userAgent)
        }
    }
}