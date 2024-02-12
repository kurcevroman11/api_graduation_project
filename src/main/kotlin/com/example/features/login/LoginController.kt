package com.example.features.login

import com.example.database.user.UserModule
import com.example.features.register.RegisterResponseRemote
import com.example.utils.TokenManager
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin() {
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = UserModule.fetchUser(receive.login)

        if(userDTO == null){
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if(userDTO.password == receive.password){
                val tokenManager = TokenManager()

                val tokenLong = tokenManager.generateTokenLong2(receive.login, userDTO.id)
                transaction {
                    addLogger(StdOutSqlLogger)

                    UserModule.update({ UserModule.login eq receive.login }) {
                        it[token_long] = tokenLong
                    }
                }


                call.respond(
                    RegisterResponseRemote( tokenLong = tokenLong )
                )
                call.respond(HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}