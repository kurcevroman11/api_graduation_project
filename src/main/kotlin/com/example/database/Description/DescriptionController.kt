package com.example.db.Description

import com.example.db.Description.DescriptionModel.deletDescription
import com.example.db.Description.DescriptionModel.getDescription
import com.example.db.Description.DescriptionModel.getDescriptionAll
import com.example.db.Description.DescriptionModel.insertDescription
import com.example.db.Description.DescriptionModel.readImegeByte
import com.example.db.Description.DescriptionModel.writeImegeByte
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import mu.KotlinLogging

const val MAX_FILE_SIZE = 1048576 * 20 // 20МБ

@Serializable
data class DescriptionDTOAPI(   val id:Int?,
                                val content:String?,
                                val file_resources: MutableList<fileClass>?,
                                val photo_resources:MutableList<photoClass>?,
                                val video_resources: MutableList<videoClass>?)
@Serializable
data class photoClass(val filename: String?, val filetype: String?, val photo: ByteArray)
@Serializable
data class fileClass(val filename:String?, val filetype:String?, val photo : ByteArray)
@Serializable
data class videoClass(val filename:String, val filetype:String, val photo : ByteArray)

private val logger = KotlinLogging.logger {}
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
                    val gson = Gson()
                    val description_photo = gson.toJson(descriptionDTOAPI)
                    logger.info { "Фото обработаны в Json" }
                    call.respond(description_photo)
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

            put("/photo/{id}") {


                val descriptionId = call.parameters["id"]?.toIntOrNull()
                if (descriptionId != null) {
                    val description = call.receive<String>()
                    val request = call.request
                    val contentLength = request.contentLength()



                    logger.info { "Объем запроса:${(contentLength?.div(1024) )?.div(124)} MB" }
                    if (contentLength!! < MAX_FILE_SIZE){
                        val gson = Gson()
                        val descriptionDTO = getDescription(descriptionId)





                        val descriptionDTOAPI = gson.fromJson(description, DescriptionDTOAPI::class.java)

                        writeImegeByte(descriptionDTOAPI.photo_resources!!,descriptionDTO.photo_resources!!)

                        call.respond(HttpStatusCode.OK)
                    }
                    else{
                        call.respond(HttpStatusCode.BadRequest, "Сильно большой объем фото")
                    }

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