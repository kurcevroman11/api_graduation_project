package com.example.database.UserRoleProject

import kotlinx.serialization.Serializable
@Serializable
class UserRoleProjectDTO(
    val id:Int?,
    val users: Int?,
    val role: Int?,
    val task:Int, ){
    constructor() : this(
        id = null,
        users = null,
        role = null,
        task = 0,
    )
}


