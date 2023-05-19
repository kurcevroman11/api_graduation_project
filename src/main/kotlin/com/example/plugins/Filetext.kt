package com.example.plugins

import com.google.gson.Gson
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable


@Serializable
data class Test(val text:String, val photo: String)

fun Application.filetext() {

    routing {
        get("/filetext") {

            val test= Test("Hello word!", "http://2d8f-178-76-226-239.ngrok-free.app/photo")

            val gson = Gson()

            val json = gson.toJson(test)
            call.respond(json)
        }
    }
}
