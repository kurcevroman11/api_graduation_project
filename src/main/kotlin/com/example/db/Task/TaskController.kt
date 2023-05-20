package com.example.db.Task

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.example.db.Task.TaskModel
import com.example.db.Task.TaskModel.deletTask
import com.example.db.Task.TaskModel.getTask
import com.example.db.Task.TaskModel.getTaskAll
import com.example.db.Task.TaskModel.insert
import com.example.db.Task.TaskModel.updateTask
import com.example.plugins.User
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*

fun Application.TaskContriller() {
    routing {
        route("/task") {
            get {
                val tastDTO = getTaskAll()
                call.respond(tastDTO)
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




