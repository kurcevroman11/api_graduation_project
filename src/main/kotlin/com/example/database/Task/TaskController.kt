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
import com.example.plugins.createMedia
import com.example.plugins.decodeJwtToken
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

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
                if (taskId != null) {

                if (apiToken != null) {
                    val claimsSet = decodeJwtToken(apiToken)
                    if (claimsSet != null) {
                        val currentDate = Date()
                        val isTokenExpired = claimsSet.expirationTime?.before(currentDate) ?: false
                        if (!isTokenExpired)
                        {
                           val user = getUserToLogin(claimsSet.subject)
                           val role = getUserProjectRole(taskId!!, user?.id!!)
                            if(role == 3){
                                val task = call.receive<String>()
                                val gson = Gson()

                                val taskDTO = gson.fromJson(task, TaskDTO::class.java)
                                call.respond(updateTask(taskId, taskDTO))
                            }
                            else{
                                call.respond("У пользователя нет доступа")
                            }
                        }
                        else{
                            call.respond("Токена не дышит")
                        }
                    }
                    else
                    {
                        call.respond("Токена не дишифрируеться")
                    }

                } else {
                    call.respond("Токена нет")
                }
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



