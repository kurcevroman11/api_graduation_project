package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.main01() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
    }

    routing {
        get("/tasGDFGk/project") {
            call.respondText("Hello World!")
        }
    }
}