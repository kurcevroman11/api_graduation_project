package com.example.database.Status

import com.example.database.Role.RoleModel
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.StatusContriller() {
    routing {
        route("/status") {
            get {
                val statusDTO = StatusModel.getStatusAll()
                val gson = Gson()

                val description = gson.toJson(statusDTO)

                call.respond(description)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    val statusDTO = StatusModel.getStatus(id)
                    call.respond(statusDTO!!)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
        }
    }
}