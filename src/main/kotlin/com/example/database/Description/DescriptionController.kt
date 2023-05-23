package com.example.db.Description



import com.example.db.Description.DescriptionModel.deletDescription
import com.example.db.Description.DescriptionModel.getDescription
import com.example.db.Description.DescriptionModel.getDescriptionAll
import com.example.db.Description.DescriptionModel.insertDescription
import com.example.db.Description.DescriptionModel.updateDescription
import com.example.db.Task.TaskDTO
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.DescriptionContriller() {
    routing {
        route("/description") {
            get {
                val descriptionDTO = getDescriptionAll()
                val gson = Gson()

                val description = gson.toJson(descriptionDTO)

                call.respond(description)
            }

            get("/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                if (descriptionId != null) {
                    val descriptionDTO = getDescription(descriptionId)
                    call.respond(descriptionDTO!!)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }

            post {
                val description = call.receive<String>()
                val gson = Gson()

                val name = gson.fromJson(description, DescriptionDTO::class.java)
                insertDescription(name)
                call.respond(HttpStatusCode.Created)
            }

            put("/{id}") {

                val descriptionId = call.parameters["id"]?.toIntOrNull()
                if (descriptionId != null) {
                    val description = call.receive<String>()
                    val gson = Gson()

                    val descriptionDTO = gson.fromJson(description, DescriptionDTO::class.java)
                    call.respond(updateDescription(descriptionId, descriptionDTO))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

            }

            delete("/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                if (descriptionId != null) {
                    call.respond(deletDescription(descriptionId), "Delete")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }


            }

        }
    }
}