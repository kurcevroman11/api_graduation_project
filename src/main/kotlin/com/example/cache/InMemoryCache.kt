package com.example.cache

import com.example.features.register.RegisterReciveRemote

data class TokenCache(
    val login: String,
    val token: String
)

object InMemoryCache{
    val userList: MutableList<RegisterReciveRemote> = mutableListOf()
    val token: MutableList<TokenCache> = mutableListOf()
}