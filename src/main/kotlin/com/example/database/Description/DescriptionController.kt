package com.example.db.Description

import com.example.db.Description.DescriptionModel.deletDescription
import com.example.db.Description.DescriptionModel.getDescription
import com.example.db.Description.DescriptionModel.getDescriptionAll
import com.example.db.Description.DescriptionModel.insertDescription
import com.example.db.Description.DescriptionModel.readImegeByte
import com.example.db.Description.DescriptionModel.updateDescription
import com.example.db.Description.DescriptionModel.writeImegeByte
import com.example.db.Task.TaskDTO
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class DescriptionDTOAPI(   val id:Int?,
                                val content:String?,
                                val file_resources: MutableList<ByteArray>?,
                                val photo_resources:MutableList<ByteArray>?,
                                val video_resources: MutableList<ByteArray>?)
fun Application.DescriptionContriller() {
    routing {
        route("/description") {
            get {
                val descriptionDTOAPI = mutableListOf<DescriptionDTOAPI>()
                val descriptionDTO = getDescriptionAll()
                val gson = Gson()

                for (description in descriptionDTO)
                {
                    val photoResources = description.photo_resources
                    val photoBytes = if (photoResources != null) readImegeByte(photoResources) else null
                    descriptionDTOAPI.add(DescriptionDTOAPI(description.id,description.content, null, photoBytes,null))
                }

                val description = gson.toJson(descriptionDTOAPI)

                call.respond(description)
            }

            get("/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()

                if (descriptionId != null) {
                    val description = getDescription(descriptionId)
                    val  descriptionDTOAPI = DescriptionDTOAPI(description.id,description.content, null , readImegeByte(description.photo_resources!!),null)
                    call.respond(descriptionDTOAPI!!)
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

                    val descriptionDTO = getDescription(descriptionId)



                    val descriptionDTOAPI = gson.fromJson(description, DescriptionDTOAPI::class.java)

                    writeImegeByte(descriptionDTOAPI.photo_resources!!,descriptionDTO.photo_resources!!)

                    call.respond(HttpStatusCode.OK)
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