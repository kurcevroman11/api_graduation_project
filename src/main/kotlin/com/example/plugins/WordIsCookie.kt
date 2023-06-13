package com.example.plugins

import com.example.database.user.UserModule.fetchUserID
import com.google.gson.Gson
import io.ktor.http.*
import io.ktor.http.Cookie
import io.ktor.http.HttpHeaders.Date
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import kotlinx.serialization.Serializable
import mu.KotlinLogging
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

private val logger = KotlinLogging.logger {}
data class CartSession(val userID: String, val productIDs: MutableList<Int>)

@Serializable
data class UserData(val username: String, val email: String)
fun Application.cookie() {
    routing {
        route("/cookie") {
            post() {
                val user = call.receive<String>()
                val gson = Gson()
                val userData = gson.fromJson(user, UserData::class.java)
                val username = userData.username

                val cookieValue = call.request.cookies["login"]

                if (cookieValue != null) {
                    // Проверка времени истечения куки
                    val expirationTime = getExpirationTimeFromCookie(cookieValue)

                        if (expirationTime != null && expirationTime <= LocalDateTime.now()) {
                            // Куки истекли
                            call.respond(HttpStatusCode.Unauthorized, "Нет")
                        } else {
                            logger.info { "$cookieValue" }
                            // Куки действительны
                            call.response.cookies.append("username", username)
                            call.respond(HttpStatusCode.OK, "Да $cookieValue")

                        }
                } else {
                    // Куки не найдены
                    call.respond(HttpStatusCode.Unauthorized, "Cookie not found")
                }
            }

            get("/login") {
                call.sessions.set(CartSession(userID = "123", productIDs = mutableListOf(1, 3, 7, 2)))
                call.respondRedirect("/cookie/cart")
            }


            get("/cart") {
                val cartSession = call.sessions.get<CartSession>()
                if (cartSession != null) {
                    call.respondText("Product IDs: ${cartSession.productIDs}")
                } else {
                    call.respondText("Your basket is empty.")
                }
            }


            get("/logout") {
                call.sessions.clear<CartSession>()
                call.respondRedirect("/cookie/cart")
            }


//            get(){
//
//
//
//                val token = fetchUserID(2)
//                call.response.cookies.append(
//                    name = "token",
//                    value = token!!.token_long,
//                    maxAge = 5 * 60L, // Время жизни в секундах
//                    httpOnly = true)
//
//                call.response.cookies.append(
//                    name = "login",
//                    value = token!!.login,
//                    maxAge = 60L, // Время жизни в секундах
//                    httpOnly = true)
//                call.respond(HttpStatusCode.OK, "Держи токен")
//            }

        }


    }
}

fun getExpirationTimeFromCookie(cookieValue: String): LocalDateTime? {
    val expirationPattern = "yyyy-MM-dd HH:mm:ss" // Формат времени истечения куки
    val formatter = DateTimeFormatter.ofPattern(expirationPattern)
    return try {
        LocalDateTime.parse(cookieValue, formatter)
    } catch (e: Exception) {
        null // Обработка ошибок парсинга времени истечения куки
    }
}
