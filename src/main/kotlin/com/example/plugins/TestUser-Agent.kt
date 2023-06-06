package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.main_3() {
    routing {
        get("/") {
            val userAgent = call.request.header(HttpHeaders.UserAgent)
            if (userAgent?.contains("mobile", ignoreCase = true) == true) {
                // ������ ������ �� ���������� �������
                call.respondText("Mobile client detected")
            } else {
                // ������ ������ �� ��������
                call.respondText("Browser detected")
            }
        }
    }
}