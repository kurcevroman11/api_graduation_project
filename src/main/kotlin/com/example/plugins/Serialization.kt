package com.example.plugins



import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.contentnegotiation.*


fun Application.configureSerialization() {
  install(ContentNegotiation){
    json()
  }

  install(Authentication) {
    jwt {
      realm = "Access to 'hello'"
      verifier(JWTConfig.verifier)
      validate { credentials ->
        if (credentials.payload.audience.contains("http://0.0.0.0:8080/hello")) {
          JWTPrincipal(credentials.payload)
        } else {
          null
        }
      }
    }
  }
}








