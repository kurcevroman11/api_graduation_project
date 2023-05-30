package com.example.plugins

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import com.nimbusds.jwt.JWT
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import io.ktor.http.*
import io.ktor.server.response.*
import javax.xml.crypto.Data


fun Application.tokenUser() {
    routing {
        get("/token") {
            val apiToken = call.request.header(HttpHeaders.Authorization)?.removePrefix("Bearer ")

            if (apiToken != null) {
                val claimsSet = decodeJwtToken(apiToken)
                if (claimsSet != null) {

                    // Обработка раскодированных данных токена
                    val subject = claimsSet.subject
                    val  expirationTime = claimsSet.expirationTime
                    // Обработка ошибки декодирования токена
                    call.respond("API token: $apiToken \n" +
                            "Субьект:$subject \n" +
                            "Время жития:$expirationTime")
                }

            } else {
                call.respond("Missing API token")
            }
        }
    }
}


