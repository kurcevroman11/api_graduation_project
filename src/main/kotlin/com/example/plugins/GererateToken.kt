package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

fun generateTokenShort(username: String): String {

    val issuer = "BeerJesus"
    val audience = "Developers"
    val secret = "VRS"

    val algorithm = Algorithm.HMAC256(secret)
    val token = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(username)
        .withExpiresAt(Date(System.currentTimeMillis() + (5 * 60 * 1000) )) // Установка срока действия токена (1 час)
        .sign(algorithm)

    return token
}

fun generateTokenLong(username: String): String {

    val issuer = "BeerJesus"
    val audience = "Developers"
    val secret = "VRS"

    val algorithm = Algorithm.HMAC256(secret)
    val token = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(username)
        .withExpiresAt(Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // Установка срока действия токена (1 час)
        .sign(algorithm)

    return token
}