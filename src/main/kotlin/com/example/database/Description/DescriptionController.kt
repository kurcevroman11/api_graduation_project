package com.example.db.Description

import com.example.db.Description.DescriptionModel.deletDescription
import com.example.db.Description.DescriptionModel.getDescription
import com.example.db.Description.DescriptionModel.getDescriptionAll
import com.example.db.Description.DescriptionModel.insertDescription
import com.example.db.Description.DescriptionModel.readFileByte
import com.example.db.Description.DescriptionModel.readImegeByte
import com.example.db.Description.DescriptionModel.writeFileByte
import com.example.db.Description.DescriptionModel.writeImegeByte
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import java.io.File

const val MAX_FILE_SIZE: Int = 1048576 * 20 // 20МБ

@Serializable
data class DescriptionDTOAPI(   val id:Int?,
                                val content:String?,
                                val file_resources: MutableList<fileClass>?,
                                val photo_resources:MutableList<photoClass>?,
                                val video_resources: MutableList<videoClass>?)
@Serializable
data class photoClass(val filename: String?, val filetype: String?, val photo: ByteArray)
@Serializable
data class fileClass(val filename:String?, val filetype:String?, val file: ByteArray)
@Serializable
data class videoClass(val filename:String, val filetype:String, val video: ByteArray)

@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)

private val logger = KotlinLogging.logger {}
fun Application.DescriptionContriller() {
    routing {
        route("/description") {
            get("/download/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                if (descriptionId != null) {
                    val descriptionDTO = getDescription(descriptionId)
                    val imegeString = readImegeByte(descriptionDTO.photo_resources!!)
                    for (imege in imegeString) {
                        call.response.header(
                            HttpHeaders.ContentDisposition,
                            ContentDisposition.Attachment.withParameter(
                                ContentDisposition.Parameters.FileName,
                                "${imege.filename}"
                            )
                                .toString()
                        )
                        //call.respondFile(imege)
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }

            var fileDescription = ""
            var fileName = ""

            post("/upload/photo/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                val contentLength = call.request.header(HttpHeaders.ContentLength).toString().toInt()
                if (descriptionId != null) {
                    val descriptionDTO = getDescription(descriptionId)
                    if (contentLength!! < MAX_FILE_SIZE) {
                        val multipartData = call.receiveMultipart()

                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    fileDescription = part.value
                                }

                                is PartData.FileItem -> {
                                    fileName = part.originalFileName as String
                                    val fileBytes = part.streamProvider().readBytes()
                                    File(descriptionDTO.photo_resources + fileName).writeBytes(fileBytes)
                                }

                                else -> {}
                            }
                            part.dispose()
                        }
                        call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Сильно большой файл!")
                    }

                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }

            post("/upload/file/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                val contentLength = call.request.header(HttpHeaders.ContentLength).toString().toInt()
                if (descriptionId != null) {
                    val descriptionDTO = getDescription(descriptionId)
                    if (contentLength!! < MAX_FILE_SIZE) {
                        val multipartData = call.receiveMultipart()

                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    fileDescription = part.value
                                }

                                is PartData.FileItem -> {
                                    fileName = part.originalFileName as String
                                    val fileBytes = part.streamProvider().readBytes()
                                    File(descriptionDTO.file_resources + fileName).writeBytes(fileBytes)
                                }

                                else -> {}
                            }
                            part.dispose()
                        }
                        call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Сильно большой файл!")
                    }

                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }

            post("/upload/video/{id}") {
                val descriptionId = call.parameters["id"]?.toIntOrNull()
                val contentLength = call.request.header(HttpHeaders.ContentLength).toString().toInt()
                if (descriptionId != null) {
                    val descriptionDTO = getDescription(descriptionId)
                    if (contentLength!! < MAX_FILE_SIZE) {
                        val multipartData = call.receiveMultipart()

                        multipartData.forEachPart { part ->
                            when (part) {
                                is PartData.FormItem -> {
                                    fileDescription = part.value
                                }

                                is PartData.FileItem -> {
                                    fileName = part.originalFileName as String
                                    val fileBytes = part.streamProvider().readBytes()
                                    File(descriptionDTO.video_resources + fileName).writeBytes(fileBytes)
                                }

                                else -> {}
                            }
                            part.dispose()
                        }
                        call.respondText("$fileDescription is uploaded to 'uploads/$fileName'")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Сильно большой файл!")
                    }

                }else {
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