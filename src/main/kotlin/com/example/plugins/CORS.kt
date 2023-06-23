package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.configureCORS(){
    install(CORS) {
        allowHeader(HttpHeaders.CacheControl)
        exposeHeader(HttpHeaders.CacheControl)
        allowHeader(HttpHeaders.ContentType)
        allowHeader("Access-Control-Allow-Origin")
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.AccessControlAllowMethods)
        allowHeader(HttpHeaders.AccessControlAllowCredentials)
        allowHeader(HttpHeaders.Authorization)

        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowCredentials = true
        allowSameOrigin = true

        anyHost()
    }
}