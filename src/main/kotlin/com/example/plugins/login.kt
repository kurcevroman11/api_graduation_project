package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.*

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.google.gson.Gson

fun Application.login() {
    routing {
        post("/login") {
            try {
                val json = call.receive<String>()

                val gson = Gson()

                val credentials = gson.fromJson(json,LoginCredentials::class.java)

                // Проверка правильности логина и пароля (пример)
                if (isValidCredentials(credentials)) {
                    // Генерация JWT-токена
                    val token = generateToken(credentials.username)
                    call.respond(HttpStatusCode.OK, token)
                } else {
                    call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
                }
            } catch (e: ContentTransformationException) {
                call.respond(HttpStatusCode.BadRequest, "Invalid request")
            }
        }
    }
}

data class LoginCredentials(val username: String, val password: String)

fun isValidCredentials(credentials: LoginCredentials): Boolean {
    // Проверка логина и пароля в базе данных или другом месте
    return credentials.username == "1" && credentials.password == "1"
}

fun generateToken(username: String): String {
    val issuer = "your-issuer"
    val audience = "your-audience"
    val secret = "your-secret-key"

    val algorithm = Algorithm.HMAC256(secret)
    val token = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(username)
        .withExpiresAt(Date(System.currentTimeMillis() + 3600000)) // Установка срока действия токена (1 час)
        .sign(algorithm)

    return token
}