package com.example.database.UserRoleProject

import kotlinx.serialization.Serializable
@Serializable
class UserRoleProjectDTO(
    val id:Int?,
    val userid: Int?,
    val roleid: Int?,
    val projectid:Int?,
    val type_of_activityid: Int?,
    val score: Int?,
    val current_task_id:Int?
)


