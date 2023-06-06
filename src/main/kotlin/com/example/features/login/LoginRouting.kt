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
    install(Sessions) {
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<RegisterResponseRemote>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
        }
    }

    routing {
        post("/login") {
            val userAgent = call.request.header(HttpHeaders.UserAgent)
            val loginController = LoginController(call)
            loginController.performLogin(userAgent)
        }
        options("/login") {
            val userAgent = call.request.header(HttpHeaders.UserAgent)
            val loginController = LoginController(call)
            loginController.performLogin(userAgent)
        }
    }
}