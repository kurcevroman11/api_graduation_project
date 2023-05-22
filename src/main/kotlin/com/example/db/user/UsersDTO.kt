package com.example.database.user

import kotlinx.serialization.Serializable

@Serializable
class UsersDTO (
    val login: String,
    val password: String,
    val token_short: String,
    val token_long: String,
    val personId: Int? = null
)
