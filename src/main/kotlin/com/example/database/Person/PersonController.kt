package com.example.database.Person

import com.example.database.user.UserModule
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.PersonContriller() {
    routing {
        authenticate("auth-jwt") {
            route("/person") {
                get {
                    val personDTO = PersonModule.fetchAllPerson()
                    call.respond(personDTO)
                }

                get("/single"){
                    val principle = call.principal<JWTPrincipal>()
                    val userId = principle!!.payload.getClaim("userId").asInt()

                    val user = UserModule.fetchUserID(userId)

                    if (user!!.personId != null) {
                        val personDTO = PersonModule.fetchPersonID(user!!.personId!!)
                        call.respond(personDTO!!)
                    }else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }


                get("/{id}") {
                    val personId = call.parameters["id"]?.toIntOrNull()
                    if (personId != null) {
                        val personDTO = PersonModule.fetchPersonID(personId)
                        call.respond(personDTO!!)
                    }else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }

                post {
                    val person = call.receive<String>()
                    val gson = Gson()

                    val personDTO = gson.fromJson(person, PersonDTO::class.java)
                    PersonModule.insertPerson(personDTO)
                    call.respond(HttpStatusCode.Created)
                }

                put("/{id}") {

                    val personID = call.parameters["id"]?.toIntOrNull()
                    if (personID != null) {
                        val person = call.receive<String>()
                        val gson = Gson()

                        val personDTO = gson.fromJson(person, PersonDTO::class.java)
                        call.respond(PersonModule.updatePerson(personID, personDTO))
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }

                delete("/{id}") {
                    val personId = call.parameters["id"]?.toIntOrNull()
                    if (personId != null) {
                        call.respond(PersonModule.deletePerson(personId), "Delete")
                    } else {
                        call.respond(HttpStatusCode.BadRequest, "Invalid ID format.")
                    }
                }
            }
        }
    }
}
