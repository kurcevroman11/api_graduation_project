package com.example.database.Person

import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.PersonContriller() {
    routing {
        route("/person") {
            get {
                val personDTO = PersonModule.fetchAllPerson()
                val gson = Gson()

                val description = gson.toJson(personDTO)
                call.respond(description)
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
