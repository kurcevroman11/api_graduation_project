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
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging
import java.util.*

private val logger = KotlinLogging.logger {}
fun Application.TaskContriller() {
    routing {
        authenticate("auth-jwt") {
            route("/task") {
                // ����� ���� �����
                get {
                    val taskDTOs = getTaskAll()
                    call.respond (HttpStatusCode.OK, taskDTOs)
                }

                //����� ���� ��������
                get("/project") {
                    val taskDTO = getProjectAll()
                    call.respond(taskDTO)
                }

                //����� ������������� ����
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
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }

                }

                //�������� ���������
                post("/{id}") {
                    val task = call.receive<String>()
                    val gson = Gson()
                    val taskId = call.parameters["id"]?.toInt()

                    var name = gson.fromJson(task, TaskDTO::class.java)

                    var taskPerent = getTask(taskId!!)
                    val id = insertandGetIdTask(name)

                    if (name.generation != null) {
                        name.generation = taskPerent!!.generation!! + 1
                    }
                    var sum = 0

                    if (name.generation!! == 2) {
                        taskPerent!!.scope = if (name!!.scope!! > taskPerent!!.scope!!) {
                            name!!.scope!!
                        } else {
                            taskPerent!!.scope!!
                        }
                        logger.info { "����� ����� 2 ��������� = ${taskPerent!!.scope}" }
                    } else {
                        val listDownTask = getDownTask(taskId)

                        sum += name.scope!!
                        for (task in listDownTask) {
                            sum += task.scope!!
                        }
                        taskPerent!!.scope = sum
                        logger.info { "����� ����� 3 ��������� = ${taskPerent!!.scope}" }
                    }

                    name.parent = taskId
                    name.description = createMedia(id.toString()).toInt()
                    name.status = 2

                    updateTask(id.toInt(), name)

                    updateTask(taskPerent!!.id!!, taskPerent!!)

                    while (taskPerent?.parent != null) {
                        name = taskPerent

                        taskPerent = getTask(taskPerent?.parent!!)

                        taskPerent!!.scope = if (name!!.scope!! > taskPerent!!.scope!!) {
                            name!!.scope!!
                        } else {
                            taskPerent!!.scope!!
                        }

                        updateTask(taskPerent.id!!, taskPerent)
                    }
                    call.respond(HttpStatusCode.Created)
                }

                //�������� ������
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
                //�������� ������ ��� ���
                options {
                    val task = call.receive<String>()
                    val gson = Gson()
                    val name = gson.fromJson(task, TaskDTO::class.java)
                    name.description = createMedia(name.name).toInt()
                    name.status = 2

                    insert(name)
                    call.respond(HttpStatusCode.Created)
                }

                //����������� ������
                put("/{id}") {

                    val apiToken = call.request.header(HttpHeaders.Authorization)?.removePrefix("Bearer ")
                    val taskId = call.parameters["id"]?.toIntOrNull()

                    val status = authorization_user(apiToken, taskId, 3)//�������� �������
                    if (status.code == HttpStatusCode.OK) {
                        val task = call.receive<String>()
                        val gson = Gson()

                        val taskDTO = gson.fromJson(task, TaskDTO::class.java)
                        call.respond(status.code, updateTask(taskId!!, taskDTO))
                    }

                    call.respond(status.code, status.description)
                }

                //�������� ������
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




