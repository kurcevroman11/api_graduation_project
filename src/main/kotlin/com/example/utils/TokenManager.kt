package com.example.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.plugins.User
import com.example.plugins.sekret
import io.ktor.server.config.*
import java.util.*

class TokenManager() {
    val issuer = "BeerJesus"
    val audience = "Developers"
    val secret = "SEKRET"
    val expirationDate = System.currentTimeMillis() + 1200000;

    fun generateTokenLong2(username: String, userId: Int?): String {
        val token = JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", username)
            .withClaim("userId", userId)
            .withExpiresAt(Date(System.currentTimeMillis() + 60000 * 60))
            .sign(Algorithm.HMAC256(secret))
        return token
    }


    fun verifyJWTToken(): JWTVerifier {
        return JWT.require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }
}