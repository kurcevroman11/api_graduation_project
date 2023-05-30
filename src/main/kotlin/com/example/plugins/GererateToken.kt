package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import io.github.cdimascio.dotenv.Dotenv
import java.util.*

val dotenv: Dotenv = Dotenv.configure().load()


val sekret: String? = dotenv["SEKRET"]

fun generateTokenShort(username: String): String {

    val issuer = "BeerJesus"
    val audience = "Developers"
    val secret = sekret

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
    val secret = sekret

    val algorithm = Algorithm.HMAC256(secret)
    val token = JWT.create()
        .withIssuer(issuer)
        .withAudience(audience)
        .withSubject(username)
        .withExpiresAt(Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))) // Установка срока действия токена (1 день)
        .sign(algorithm)

    return token
}

fun decodeJwtToken(token: String): JWTClaimsSet? {
    try {
            val jwt: com.nimbusds.jwt.JWT = JWTParser.parse(token)
            return jwt.jwtClaimsSet
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}