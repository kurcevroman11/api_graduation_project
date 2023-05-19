package com.example.features.register


import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import java.util.*
import  com.example.database.user.usser
import com.example.database.user.UsersDTO
import com.example.plugins.generateTokenLong
import com.example.plugins.generateTokenShort
import com.example.utils.isValidPassword

import io.ktor.server.request.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction


class RegisterController(val call: ApplicationCall) {

   suspend fun registerNewUser(){


       val registerReciveRemote = call.receive<RegisterReciveRemote>()
       if(!registerReciveRemote.password.isValidPassword()){
           call.respond(HttpStatusCode.BadRequest, "Email is not")
       }

       var tokenShort : String = ""
       var tokenLong : String = ""

       val userDTO = usser.fetchUser(registerReciveRemote.login)
       if(userDTO != null){
           call.respond(HttpStatusCode.Conflict, "User already exists")
       }
       else {
           tokenShort = generateTokenShort(registerReciveRemote.login)
           tokenLong = generateTokenLong(registerReciveRemote.login)
           transaction {
               addLogger(StdOutSqlLogger)

               usser.insert(
                   UsersDTO(
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