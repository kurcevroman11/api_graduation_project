package com.example.plugins

import com.example.utils.TokenManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureSerialization() {
  install(ContentNegotiation){
    json()
  }

  val tokenManager = TokenManager()

  install(Authentication) {
    jwt("auth-jwt") {
      verifier(tokenManager.verifyJWTToken())
      realm = "Hi!"
      validate { jwtCredential ->
        if(jwtCredential.payload.getClaim("username").asString().isNotEmpty()) {
          JWTPrincipal(jwtCredential.payload)
        } else {
          null
        }
      }
    }
  }
}








