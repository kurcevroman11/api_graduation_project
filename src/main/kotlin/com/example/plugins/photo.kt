package com.example.plugins

import com.google.gson.Gson
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import kotlinx.serialization.Serializable
import java.io.File



fun Application.photo() {

    routing {
        get("/photo") {

            val photoFile = File("C:\\Users\\sergk\\OneDrive\\Рабочий стол\\Test — копия\\src\\main\\resources\\photo\\Hello.jpg")

            call.respond(LocalFileContent(photoFile))
        }
        }
    }
