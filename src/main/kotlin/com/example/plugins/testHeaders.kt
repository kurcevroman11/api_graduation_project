package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.heades() {
    routing {
        get("/TestHeader") {
            val authorization = call.request.header(HttpHeaders.Authorization)
            println()
            println(authorization)
            println()

            val host = call.request.header(HttpHeaders.Host)
            println()
            println(host)
            println()

            val user = call.request.header(HttpHeaders.UserAgent)
            println()
            println(user)
            println()

            call.respondText("Test")
        }
    }
}