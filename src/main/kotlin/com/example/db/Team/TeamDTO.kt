package com.example.db.Team

import kotlinx.serialization.Serializable

@Serializable
class TeamDTO(
    val id:Int?,
    val users: Int?,
    val task:Int,
    val evaluation: String?,
    val time: String?
){
}