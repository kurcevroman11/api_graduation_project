package com.example.plugins


import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.google.gson.Gson
import io.ktor.server.response.*

data class Numbers(val a: Int, val b: Int)


fun Application.SumNambers(){

        routing {
            post("/sum") {
                val json = call.receive<String>()
                val h = call.request.headers
                val gson = Gson()

                val numbers = gson.fromJson(json,Numbers::class.java)

                var sum = numbers.a + numbers.b

                call.respond("${numbers.a} + ${numbers.b} = $sum")

            }
        }


}


