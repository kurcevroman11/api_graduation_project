package com.example.features.login

import com.example.database.user.UserModule
import com.example.features.register.RegisterResponseRemote
import com.example.plugins.generateTokenLong
import com.example.plugins.generateTokenShort
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin(){
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
                call.respond(
                    RegisterResponseRemote( tokenLong = tokenLong )
                )
            } else{
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}