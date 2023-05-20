package com.example.database.user

class UsersDTO (
    val login: String,
    val password: String,
    val token_short: String,
    val token_long: String,
    val personId: Int? = null
)
