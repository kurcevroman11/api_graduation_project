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
import com.example.db.Task.TaskModel.getDownTask
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
import mu.KotlinLogging
import java.util.*


data class TaskDependence(
    val name: String,
    val dependence: List<Int>?,
)

private val logger = KotlinLogging.logger {}
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
        authenticate("auth-jwt") {
            route("/task") {
                // Вывод всех задач
                get {
                    val taskDTOs = getTaskAll()
                    call.respond (HttpStatusCode.OK, taskDTOs)
                }

                //Вывод всех проектов
                get("/project") {
                    val taskDTO = getProjectAll()
                    call.respond(taskDTO)
                }

                //Вывод определенного айди
                get("/{id}") {
                    val taskId = call.parameters["id"]?.toIntOrNull()
                    if (taskId != null) {
                        val tastDTO = getTask(taskId)
                        call.respond(tastDTO!!)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }

                get("/downtask/{id}") {
                    val taskId = call.parameters["id"]?.toIntOrNull()
                    if (taskId != null) {
                        val tastDTO = getDownTask(taskId)
                        val gson = Gson()
                        val task = gson.toJson(tastDTO)
                        call.respond(task)
                    }else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }

            }

                get("/downtask/{id}") {
                    val taskId = call.parameters["id"]?.toIntOrNull()
                    if (taskId != null) {
                        val tastDTO = getDownTask(taskId)
                        val gson = Gson()
                        val task = gson.toJson(tastDTO)
                        call.respond(task)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }

            }

            get("/calculation/{id}") {
                val taskId = call.parameters["id"]?.toIntOrNull()

                var score = 0
                val taskList = mutableListOf<TaskDTO>()
                val downTaskList = mutableListOf<TaskDTO>()
                if (taskId != null) {
                    val taskDTO = getDownTask(taskId)

                    for (item in taskDTO)
                    {
                        val task = getDownTask(item.id!!)

                        for (i in  0 until task.size)
                        {

                            if(task[i].dependence != null) {
                                score += task[i].scope!!
                            }
                            else if(task[i].scope!! > score)
                            {
                                score = task[i].scope!!
                            }

                            if(task[i].typeofactivityid == 1 || task[i].typeofactivityid == 3)
                              downTaskList.add(0,task[i])
                            else if (task[i].typeofactivityid == 2 || task[i].typeofactivityid == 4)
                              downTaskList.add(task[i])
                        }
                        item.scope = score
                        downTaskList.add(item)
                        score = 0
                        taskList.addAll(downTaskList)

                        downTaskList.clear()
                    }
                    var k = 0

                    for (item in taskList)
                    {
                        item.position = k++
                        updateTask(item.id!!, item)
                    }

                    for (item in taskList)
                    {

                        if(item.dependence != null) {
                            val regex = Regex("\\d+")
                            val values = regex.findAll(item.dependence!!.toString())
                                .map { it.value.toInt() }
                                .toList()
                            for (i in values){
                                val task = getTask(i)
                                logger.info { "Задача: ${item.id} зависит от ${task!!.id}" }
                                logger.info { "Задача: ${item.position} зависит от ${task!!.position}" }
                                if(item.position!! < task!!.position!!) {
                                    item.position = task!!.position!! + 1
                                    updateTask(item.id!!, item)
                                }
                            }
                        }
                    }




                    call.respond(taskList)
                }else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                }

            }

            get("/position/{id}"){
                val taskId = call.parameters["id"]?.toIntOrNull()
                val taskList = mutableListOf<TaskDTO>()
                val downtaskList = mutableListOf<TaskDTO>()
                if (taskId != null) {
                    val taskDTO = getDownTask(taskId)

                    for (item in taskDTO)
                    {
                        val task = getDownTask(item.id!!)

                        for (i in  0 until task.size)
                        {
                            downtaskList.add(0,task[i])
                        }
                        downtaskList.add(item)
                        taskList.addAll(downtaskList)
                        downtaskList.clear()
                    }

                    var k = 0

                    taskList.sortBy { it.position }

                    call.respond(taskList)
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
                val id = insertandGetIdTask(name)

                if(name.generation != null){
                    name.generation = taskPerent!!.generation!! + 1
                }
                var sum = 0




                if (name.generation!! == 2) {
                    taskPerent!!.scope = if (name!!.scope!! > taskPerent!!.scope!!) {
                        name!!.scope!!
                    } else {
                        taskPerent!!.scope!!
                    }
                    logger.info { "Сумма задач 2 поколения = ${taskPerent!!.scope}" }
                }
                else{
                    val listDownTask = getDownTask(taskId)

                    sum += name.scope!!
                    for(task in listDownTask)
                    {
                        sum += task.scope!!
                    }
                    taskPerent!!.scope = sum
                    logger.info { "Сумма задач 3 поколения = ${taskPerent!!.scope}" }
                }



                name.parent = taskId
                name.description = createMedia(id.toString()).toInt()
                name.status = 2

                updateTask(id.toInt(), name)


                updateTask(taskPerent!!.id!!,taskPerent!!)

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
                name.generation = 1
                name.scope = 0


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


            put("dependence/{id}") {

                val apiToken = call.request.header(HttpHeaders.Authorization)?.removePrefix("Bearer ")
                val taskId = call.parameters["id"]?.toIntOrNull()

                    val task = call.receive<String>()
                    val gson = Gson()

                    val dependence = gson.fromJson(task, TaskDependence::class.java)
                    val taskDTO = getTask(taskId!!)
                    taskDTO?.dependence = dependence.dependence.toString()
                    call.respond(updateTask(taskId!!, taskDTO!!))

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



