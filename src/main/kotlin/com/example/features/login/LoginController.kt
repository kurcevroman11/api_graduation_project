package com.example.features.login

import com.example.database.user.usser
import com.example.database.user.UsersDTO
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
import java.util.*

class LoginController(private val call: ApplicationCall) {
    suspend fun performLogin(){
        val receive = call.receive<LoginReceiveRemote>()
        val userDTO = usser.fetchUser(receive.login)
        println(userDTO)

        if(userDTO == null){
            call.respond(HttpStatusCode.BadRequest, "User not found")
        } else {
            if(userDTO.password == receive.password){
               val tokenShort = generateTokenShort(receive.login)
                val tokenLong = generateTokenLong(receive.login)
                transaction {
                    addLogger(StdOutSqlLogger)

                    usser.update({ usser.login eq receive.login }) {
                        it[token_long] = tokenLong
                        it[token_short] = tokenShort
                    }
                }
                call.respond(
                    RegisterResponseRemote(tokenShort = tokenShort,
                    tokenLong = tokenLong )
                )
            } else{
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}