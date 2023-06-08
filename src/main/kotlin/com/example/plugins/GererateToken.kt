package com.example.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.example.database.user.UserModule
import com.example.db.Task.TaskDTO
import com.example.db.Task.TaskModel
import com.example.db.UserRoleProject.UserRoleProjectModel
import com.google.gson.Gson
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.JWTParser
import io.github.cdimascio.dotenv.Dotenv
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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

data class HttpStatus(val code: HttpStatusCode, val description: String)
fun authorization_user(apiToken: String?, taskId : Int?, role : Int) : HttpStatus
{
    if (taskId != null) {
        if (apiToken != null) {
            val claimsSet = decodeJwtToken(apiToken)
            if (claimsSet != null) {
                val currentDate = Date()
                val isTokenExpired = claimsSet.expirationTime?.before(currentDate) ?: false
                if (!isTokenExpired)
                {
                    val user = UserModule.getUserToLogin(claimsSet.subject)
                    val user_role = UserRoleProjectModel.getUserProjectRole(taskId, user?.id!!)
                    if(user_role == role){
                        return (HttpStatus(HttpStatusCode.OK, "Доступ есть"))
                    }
                    else{
                        return (HttpStatus(HttpStatusCode.Forbidden, "Доступ закрыт"))
                    }
                }
                else{
                    return (HttpStatus(HttpStatusCode.BadRequest, "Токен не действителен"))
                }
            }
            else
            {
                return (HttpStatus(HttpStatusCode.BadRequest, "Токен не дишифрируется"))
            }

        } else {
            return (HttpStatus(HttpStatusCode.BadRequest, "Токен пуст"))
        }
    }  else {
    return (HttpStatus(HttpStatusCode.BadRequest, "Такая задача не существует"))
    }
}


