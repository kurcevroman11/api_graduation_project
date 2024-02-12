package com.example.db.UserRoleProject

import com.example.database.Person.PersonModule.deletePerson
import com.example.database.UserRoleProject.UserRoleProjectDTO
import com.example.db.UserRoleProject.UserRoleProjectModel.deleteURP
import com.example.db.UserRoleProject.UserRoleProjectModel.getALLUserProject
import com.example.db.UserRoleProject.UserRoleProjectModel.getTask_executors
import com.example.db.UserRoleProject.UserRoleProjectModel.getURP
import com.example.db.UserRoleProject.UserRoleProjectModel.getURPAll
import com.example.db.UserRoleProject.UserRoleProjectModel.getUserProject
import com.example.db.UserRoleProject.UserRoleProjectModel.insert
import com.example.db.UserRoleProject.UserRoleProjectModel.scheduling
import com.example.db.UserRoleProject.UserRoleProjectModel.updateURP
import com.example.pluginsimport.Exele
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.UserRoleProjectController() {
    routing {
        authenticate("auth-jwt") {
            route("/user_role_project") {

                post("/task") {
                    val principle = call.principal<JWTPrincipal>()
                    val userId = principle!!.payload.getClaim("userId").asInt()

                    val serializedList = getUserProject(userId)

                    call.respond(serializedList!!)
                }

                get {
                    val URPDTO = getURPAll()
                    val gson = Gson()
                    call.respond(gson.toJson(URPDTO))
                }

                get("/{id}") {
                    val URPId = call.parameters["id"]?.toIntOrNull()
                    if (URPId != null) {
                        val URPDTO = getURP(URPId)
                        call.respond(URPDTO!!)
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }

                    deletePerson(2)
                }

                // ������� ��� �������, � ������� ��������� ������������
                get("/task") {
                    val principle = call.principal<JWTPrincipal>()
                    val userId = principle!!.payload.getClaim("userId").asInt()

                    val authorization = call.request.header(HttpHeaders.Authorization)
                    println()
                    println(authorization)
                    println()

                    val host = call.request.header(HttpHeaders.Host)
                    println()
                    println(host)
                    println()

                    val serializedList = getUserProject(userId)

                    call.respond(serializedList!!)

                }

                get("/task_executors") {
                    getTask_executors()
                    call.respond(HttpStatusCode.Created)
                }

                get("/calendar_plan") {
                    val serializedList = scheduling()
                    call.respond(serializedList)
                }

                get("/excel") {
                    val ex = Exele()
                    ex.writeExcel("C:\\Users\\386\\OneDrive\\Документы\\Сайт\\plan.xlsx")
                    call.respond(HttpStatusCode.OK)
                }

                get("/task/{id}") {
                    val id = call.parameters["id"]?.toIntOrNull()
                    val principle = call.principal<JWTPrincipal>()
                    val userId = principle!!.payload.getClaim("userId").asInt()

                    val gson = Gson()
                    if (id != null) {
                        val URPDTO = getALLUserProject(id)
                        call.respond(gson.toJson(URPDTO))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }

                post {
                    val urp = call.receive<String>()
                    val gson = Gson()

                    val name = gson.fromJson(urp, UserRoleProjectDTO::class.java)
                    insert(name)
                    call.respond(HttpStatusCode.Created)
                }

                put("/{id}") {
                    val urpId = call.parameters["id"]?.toIntOrNull()
                    if (urpId != null) {
                        val urp = call.receive<String>()
                        val gson = Gson()

                        val URPDTO = gson.fromJson(urp, UserRoleProjectDTO::class.java)
                        call.respond(updateURP(urpId, URPDTO))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }

                delete("/{id}") {
                    val URPId = call.parameters["id"]?.toIntOrNull()
                    if (URPId != null) {
                        call.respond(deleteURP(URPId), "Delete")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }
            }
        }
    }
}