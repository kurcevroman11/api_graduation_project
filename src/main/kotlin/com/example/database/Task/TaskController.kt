package com.example.db.Task

import com.example.database.Description.DescriptionForTask.insertandGetId
import com.example.database.user.UserModule.getUserToLogin
import com.example.db.Description.DescriptionDTO
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.db.Task.TaskModel
import com.example.db.Task.TaskModel.deletTask
import com.example.db.Task.TaskModel.getProjectAll
import com.example.db.Task.TaskModel.getTask
import com.example.db.Task.TaskModel.getTaskAll
import com.example.db.Task.TaskModel.insert
import com.example.db.Task.TaskModel.updateTask
import com.example.db.UserRoleProject.UserRoleProjectModel.getUserProjectRole
import com.example.plugins.authorization_user
import com.example.plugins.createMedia
import com.example.plugins.decodeJwtToken
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Application.TaskContriller() {
    routing {
        route("/task") {

            // Обработка запросов с любым источником (CORS)
            intercept(ApplicationCallPipeline.Call) {
                if (call.request.httpMethod == HttpMethod.Options) {
                    // Обработка предварительных запросов OPTIONS

                    call.response.header(HttpHeaders.AccessControlAllowOrigin, "*")
                    call.response.header(HttpHeaders.AccessControlAllowMethods, "GET, POST, OPTIONS")
                    call.response.header(HttpHeaders.AccessControlAllowHeaders, "*")
                    call.respond(HttpStatusCode.OK)
                    finish()
                }
            }

            get {
                val taskDTO = getTaskAll()
                val gson = Gson()
                val task = gson.toJson(taskDTO)

                call.respond(task)
            }

            get("/project"){
                val taskDTO = getProjectAll()
                val gson = Gson()

                val task = gson.toJson(taskDTO)

                call.response.header(HttpHeaders.AccessControlAllowOrigin, "*")
                call.respond(task)
            }

            get("/{id}") {
                val taskId = call.parameters["id"]?.toIntOrNull()
                if (taskId != null) {
                    val tastDTO = getTask(taskId)
                    val gson = Gson()
                    val task = gson.toJson(tastDTO)
                    call.respond(task)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

            }
            post("/{id}") {
                val task = call.receive<String>()
                val gson = Gson()
                val taskId = call.parameters["id"]?.toInt()

                val name = gson.fromJson(task, TaskDTO::class.java)


                name.parent = taskId
                name.description = createMedia(name.name).toInt()
                name.status = 2

                insert(name)

                call.respond(HttpStatusCode.Created)
            }


            post {
                val task = call.receive<String>()
                val gson = Gson()


                val name = gson.fromJson(task, TaskDTO::class.java)

                name.description = createMedia(name.name).toInt()
                name.status = 2

                insert(name)
                call.respond(HttpStatusCode.Created)
            }

            put("/{id}") {
                val apiToken = call.request.header(HttpHeaders.Authorization)?.removePrefix("Bearer ")
                val taskId = call.parameters["id"]?.toIntOrNull()

                val status = authorization_user(apiToken, taskId, 3)
                if(status.code == HttpStatusCode.OK) {
                    val task = call.receive<String>()
                    val gson = Gson()

                    val taskDTO = gson.fromJson(task, TaskDTO::class.java)
                    call.respond(status.code,updateTask(taskId!!, taskDTO))
                }

                call.respond(status.code,status.description)
            }

            delete("/{id}") {
                val taskId = call.parameters["id"]?.toIntOrNull()
                if (taskId != null) {
                    call.respond(deletTask(taskId), "Delete")
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }
            }
        }
    }
}



