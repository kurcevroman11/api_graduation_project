package com.example.db.Task

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
import com.example.plugins.createMedia
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Application.TaskContriller() {
    routing {
        route("/task") {
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

                call.respond(task)
            }

            get("/{id}") {
                val taskId = call.parameters["id"]?.toIntOrNull()
                if (taskId != null) {
                    val tastDTO = getTask(taskId)
                    call.respond(tastDTO!!)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

            }
            post("/{id}") {
                val task = call.receive<String>()
                val gson = Gson()
                val taskId = call.parameters["id"]?.toIntOrNull()


                val name = gson.fromJson(task, TaskDTO::class.java)

                name.parent = taskId

                createMedia(name.name)
                insert(name)

                call.respond(HttpStatusCode.Created)
            }

            post("/{id}") {
                val task = call.receive<String>()
                val gson = Gson()
                val taskId = call.parameters["id"]?.toIntOrNull()


                val name = gson.fromJson(task, TaskDTO::class.java)

                name.parent = taskId

                insert(name)

                call.respond(HttpStatusCode.Created)
            }
            post {
                val task = call.receive<String>()
                val gson = Gson()


                val name = gson.fromJson(task, TaskDTO::class.java)
                createMedia(name.name)
                insert(name)
                call.respond(HttpStatusCode.Created)

            }

            put("/{id}") {

                val taskId = call.parameters["id"]?.toIntOrNull()
                if (taskId != null) {
                    val task = call.receive<String>()
                    val gson = Gson()

                    val taskDTO = gson.fromJson(task, TaskDTO::class.java)
                    call.respond(updateTask(taskId, taskDTO))
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

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



