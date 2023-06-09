package com.example.plugins


import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.main_03() {
    val tokenManager = TokenManager()

//    install(Authentication) {
//        jwt("auth-jwt") {
//            verifier(tokenManager.verifyJWTToken())
//            realm = "Hi!"
//            validate { jwtCredential ->
//                if(jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
//                    JWTPrincipal(jwtCredential.payload)
//                } else {
//                    null
//                }
//            }
//        }
//    }


    routing {
        authenticate("auth-jwt") {
            get("/me") {
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val expiresAt = principle.expiresAt?.time?.minus(System.currentTimeMillis())
                val userId = principle!!.payload.getClaim("userId").asInt()
                call.respondText("Hello, $username with ID $userId! Token is expired at $expiresAt ms.")
            }
        }
    }
}

