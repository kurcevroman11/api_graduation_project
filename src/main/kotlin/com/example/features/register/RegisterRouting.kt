package com.example.features.register

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRegisterRouting() {
    routing {
        authenticate("auth-jwt"){

            post("/register") {
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()

                if(username == "admin"){
                    val registerController = RegisterController(call)
                    registerController.registerNewUser()
                }
                else{
                    call.respond("Вы не админ!")
                }
            }
        }
    }
}

