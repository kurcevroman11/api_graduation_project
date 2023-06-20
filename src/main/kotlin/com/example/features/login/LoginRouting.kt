package com.example.features.login

import com.example.database.user.UsersDTO
import com.example.db.UserRoleProject.UserRoleProjectModel
import com.example.features.register.RegisterResponseRemote
import com.example.plugins.CartSession
import io.ktor.http.*
import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureLoginRouting() {
    routing {



//        options("/login") {
//            call.response.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
//            call.response.header("Access-Control-Allow-Headers", "Authorization, Content-Type")
//            call.response.header("Access-Control-Allow-Origin", "http://localhost:3000")
//            call.response.header("Access-Control-Allow-Credentials", "true")
//
//            val userAgent = call.request.header(HttpHeaders.UserAgent)
//            println()
//            println(userAgent)
//            println()
//
//            val loginController = LoginController(call)
//            loginController.performLogin(userAgent)
//        }

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