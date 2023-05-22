package com.example.db.Task


import TaskModel.deletTask
import TaskModel.getTask
import TaskModel.getTaskAll
import TaskModel.insert
import TaskModel.updateTask

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.TaskContriller() {
    routing {
        route("/task") {
            get {
                val taskDTO = getTaskAll()
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

            post {
                val task = call.receive<String>()
                val gson = Gson()

                val name = gson.fromJson(task, TaskDTO::class.java)
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




