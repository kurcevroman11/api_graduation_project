package com.example.features.register

import com.example.database.Person.PersonDTO
import com.example.database.Person.PersonForUser
import com.example.database.Person.PersonModule
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import  com.example.database.user.UserModule
import com.example.database.user.UsersDTO
import com.example.plugins.generateTokenLong
import com.example.plugins.generateTokenShort
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

class RegisterController(val call: ApplicationCall) {

    suspend fun registerNewUser(){
        val registerReciveRemote = call.receive<RegisterReciveRemote>()

        var tokenLong : String = ""

        val userDTO = UserModule.fetchUser(registerReciveRemote.login)
        if(userDTO != null){
            call.respond(HttpStatusCode.Conflict, "User already exists")
        }
        else {
            tokenLong = generateTokenLong(registerReciveRemote.login)
            transaction {
                addLogger(StdOutSqlLogger)

                val person = PersonDTO(
                    id = null,
                    surname = "",
                    name = "",
                    patronymic = ""
                )
                val personId: Int? = PersonForUser.insertandGetId(person).toInt()

                UserModule.insert(
                    UsersDTO(
                        id = null,
                        login = registerReciveRemote.login,
                        password = registerReciveRemote.password,
                        token_long = tokenLong,
                        personId = personId

                    )
                )
            }
        }
        call.respond(RegisterResponseRemote( tokenLong = tokenLong ))
    }
}