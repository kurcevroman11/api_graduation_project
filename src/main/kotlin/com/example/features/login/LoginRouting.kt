package com.example.features.login

import io.ktor.server.routing.*
import io.ktor.server.application.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
    }
}