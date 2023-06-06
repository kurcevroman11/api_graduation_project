package com.example.plugins


import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
  routing {
      route("/index") {
          install(CachingHeaders) {
              options { call, content -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 30)) }
          }

          get {
              call.respondText("Index page")
          }
      }
  }
}



