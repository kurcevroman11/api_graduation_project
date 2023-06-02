package com.example.features.login

import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
        options("/login") {
            val loginController = LoginController(call)
            loginController.performLogin()
        }
    }
}