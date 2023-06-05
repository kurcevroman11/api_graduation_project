package com.example.images

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

import kotlinx.serialization.Serializable
import java.io.File


@Serializable
data class Text(val text: String, val file: String)

fun Application.configureImageRouting() {
    routing {
        get("/image") {
            val image = File("C://Users//386//IdeaProjects//Sebbia-beck//src//main//kotlin//com//example//images//photo_2023-05-18_13-39-44.jpg")
            //val text : Text = Text("\"Hello world!\"")
            //call.respond(text)
        }
    }
}
