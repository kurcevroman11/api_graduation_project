package com.example.features.login

import com.example.cache.InMemoryCache
import com.example.cache.TokenCache
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import java.util.*

fun Application.configureLoginRouting() {
    routing {
        post("/login") {


            val loginController = LoginController(call)
            loginController.performLogin()

        }
    }
}