package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.header() {
    routing {
        get("/header") {
            val contentDispositionHeader = call.request.headers["Content-Disposition"]

            val filename = contentDispositionHeader?.substringAfter("filename=")?.trim('\"')

            if (filename != null) {
                call.respond("header filenamt: $filename,")
            }

            else {
                call.respond(HttpStatusCode.BadRequest,"Нет имени фаила" )
            }
        }
    }
}