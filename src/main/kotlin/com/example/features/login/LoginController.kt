package com.example.features.login

import com.example.database.user.usser
import com.example.database.user.UsersDTO
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
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
                val token = UUID.randomUUID().toString()
                call.respond(LoginResponseRemote(token = token))
            } else{
                call.respond(HttpStatusCode.BadRequest, "Invalid password")
            }
        }
    }
}