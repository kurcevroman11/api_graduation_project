package com.example.features.login

import ch.qos.logback.core.util.Duration
import com.example.database.user.UserModule
import com.example.features.register.RegisterResponseRemote
import com.example.plugins.CartSession
import com.example.plugins.generateTokenLong
import com.example.plugins.generateTokenShort
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin(userAgent: String?){
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = UserModule.fetchUser(receive.login)

        if(userDTO == null){
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if(userDTO.password == receive.password){
                val tokenLong = generateTokenLong(receive.login)
                transaction {
                    addLogger(StdOutSqlLogger)

                    UserModule.update({ UserModule.login eq receive.login }) {
                        it[token_long] = tokenLong
                    }
                }

                val cookie = Cookie(
                    name = "token",
                    value = tokenLong,
                    httpOnly = true
                )

                if (userAgent?.contains("mobile", ignoreCase = true) == true) {
                    // запрос пришел от мобильного клиента
                    call.respond(
                        RegisterResponseRemote( tokenLong = tokenLong )
                    )
                    //call.respondText("Mobile client detected")
                } else {
                    // запрос пришел от браузера

                    call.response.cookies.append(cookie)
                    call.respond(HttpStatusCode.OK)
                    //call.respondText("Browser detected")
                }
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}