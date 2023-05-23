package com.example.features.register

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import  com.example.database.user.UsserModel
import com.example.database.user.UsersDTO
import com.example.db.Description.DescriptionModel.autoIncrement
import com.example.plugins.generateTokenLong
import com.example.plugins.generateTokenShort
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction


class RegisterController(val call: ApplicationCall) {

    suspend fun registerNewUser(){
        val registerReciveRemote = call.receive<RegisterReciveRemote>()

        var tokenShort : String = ""
        var tokenLong : String = ""

        val userDTO = UsserModel.fetchUser(registerReciveRemote.login)
        if(userDTO != null){
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
        else {
            tokenShort = generateTokenShort(registerReciveRemote.login)
            tokenLong = generateTokenLong(registerReciveRemote.login)
            transaction {
                addLogger(StdOutSqlLogger)

                UsserModel.insert(
                    UsersDTO(
                        id = null,
                        login = registerReciveRemote.login,
                        password = registerReciveRemote.password,
                        token_short = tokenShort,
                        token_long = tokenLong
                    )
                )
            }
        }
        call.respond(RegisterResponseRemote(tokenShort = tokenShort,
            tokenLong = tokenLong ))
    }
}