package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.test() {



    routing {
        authenticate("auth-jwt") {
            get("/hello") {
                call.respondText("Доступ разрешен", status = HttpStatusCode.OK)
            }
        }
    }
}

object JWTConfig {
    private const val secret = "secret"

    val verifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience("http://0.0.0.0:8080/hello")
        .withIssuer("http://0.0.0.0:8080/")
        .build()
}