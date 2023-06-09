package com.example.db.Task

import com.example.database.Description.DescriptionForTask.insertandGetId
import com.example.database.user.UserModule.getUserToLogin
import com.example.db.Description.DescriptionDTO
import com.example.db.Task.TaskForId.insertandGetIdTask
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
import com.example.utils.TokenManager
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*

fun Application.TaskContriller() {

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)

        allowHost("client-host")
        allowHost("client-host:8081")
        allowHost("client-host", subDomains = listOf("en", "de", "es"))
        allowHost("client-host", schemes = listOf("http", "https"))

        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowCredentials = true
        allowNonSimpleContentTypes = true
        allowCredentials = true
        allowSameOrigin = true
        anyHost()
    }


    routing {
        authenticate("auth-jwt"){
        route("/task") {

//            intercept(ApplicationCallPipeline.Call) {
//                if (call.request.httpMethod == HttpMethod.Options) {
//                    call.response.header(HttpHeaders.AccessControlAllowOrigin, "*")
//                    call.response.header(HttpHeaders.AccessControlAllowMethods, "*")
//                    call.response.header(HttpHeaders.AccessControlAllowHeaders, "*")
//                    call.response.header(HttpHeaders.AccessControlAllowCredentials, "true")
//                    call.response.header(HttpHeaders.AccessControlMaxAge, "1728000")
//                    call.respond(HttpStatusCode.OK)
//                    finish()
//                }
//            }

            // Вывод всех задач
            get {
                val taskDTO = getTaskAll()
                val gson = Gson()
                val task = gson.toJson(taskDTO)

                call.response.header(HttpHeaders.ContentType, ContentType.Application.Json.toString())

                call.respond(task)

            }

            //Вывод всех проектов
            get("/project"){
                val taskDTO = getProjectAll()
                val gson = Gson()

                val task = gson.toJson(taskDTO)

                call.respond(task)
            }

            //Вывод определенного айди
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

            //Создание подзадачи
            post("/{id}") {
                val task = call.receive<String>()
                val gson = Gson()
                val taskId = call.parameters["id"]?.toInt()

                var name = gson.fromJson(task, TaskDTO::class.java)

                var taskPerent = getTask(taskId!!)

                taskPerent!!.scope = if(name!!.scope!! >  taskPerent!!.scope!!){
                    name!!.scope!!
                }
                else{
                    taskPerent!!.scope!!
                }

                val id = insertandGetIdTask(name)

                name.parent = taskId
                name.description = createMedia(id.toString()).toInt()
                name.status = 2

                updateTask(id.toInt(), name)


                updateTask(taskPerent.id!!,taskPerent)

                while (taskPerent?.parent != null)
                {
                    name = taskPerent

                    taskPerent = getTask(taskPerent?.parent!!)

                    taskPerent!!.scope = if(name!!.scope!! >  taskPerent!!.scope!!){
                        name!!.scope!!
                    }
                    else{
                        taskPerent!!.scope!!
                    }

                    updateTask(taskPerent.id!!,taskPerent)
                }







                call.respond(HttpStatusCode.Created)
            }

            //Создание задачи
            post {
                val task = call.receive<String>()
                val gson = Gson()


                val name = gson.fromJson(task, TaskDTO::class.java)

                val id = insertandGetIdTask(name)

                name.description = createMedia(id.toString()).toInt()
                name.status = 2

                updateTask(id.toInt(), name)


                call.respond(HttpStatusCode.Created)

            }
            //Создание задачи для веб
            options{
                val task = call.receive<String>()
                val gson = Gson()
                val name = gson.fromJson(task, TaskDTO::class.java)
                name.description = createMedia(name.name).toInt()
                name.status = 2

                insert(name)
                call.respond(HttpStatusCode.Created)

            }

            //Обновеление задачи
            put("/{id}") {

                val apiToken = call.request.header(HttpHeaders.Authorization)?.removePrefix("Bearer ")
                val taskId = call.parameters["id"]?.toIntOrNull()




                val status = authorization_user(apiToken, taskId, 3)//Проверка доступа
                if(status.code == HttpStatusCode.OK) {
                    val task = call.receive<String>()
                    val gson = Gson()

                    val taskDTO = gson.fromJson(task, TaskDTO::class.java)
                    call.respond(status.code,updateTask(taskId!!, taskDTO))
                }

                call.respond(status.code,status.description)


            }
            //Удаление задачи
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
}



