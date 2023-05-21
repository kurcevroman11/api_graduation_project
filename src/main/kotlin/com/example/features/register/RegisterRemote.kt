package com.example.features.register
import kotlinx.serialization.Serializable

@Serializable
data class RegisterReciveRemote(
    val login: String,
    val password: String
)

@Serializable
data class RegisterResponseRemote(
    val tokenShort : String,
    val tokenLong  : String
)