package com.example.plugins

import com.example.db.Description.DescriptionDTOAPI
import com.example.db.Description.DescriptionModel
import com.example.db.Description.DescriptionModel.readImegeByte
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

        }
    }
}
