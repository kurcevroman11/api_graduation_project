package com.example.database.Role

import com.example.db.Description.DescriptionModel
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlin.text.get

fun Application.RoleContriller() {
    routing {
        route("/role") {
            get {
                val roleDTO = RoleModel.getRoleAll()
                val gson = Gson()

                val description = gson.toJson(roleDTO)

                call.respond(description)
            }

            get("/{id}") {
                val id = call.parameters["id"]?.toIntOrNull()
                if (id != null) {
                    val roleDTO = RoleModel.getRole(id)
                    call.respond(roleDTO!!)
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
        }
    }
}