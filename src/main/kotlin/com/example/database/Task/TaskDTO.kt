package com.example.db.Task

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joda.time.DateTime
import java.time.LocalTime


@Serializable
class TaskDTO(
    var id: Int?,
    val name: String,
    var status: Int?,
    val start_date: String?,
    var scope: Int?,
    var description: Int?,
    var parent: Int?,
    val userCount: Int? = null,
    var generation : Int? = 1,
    val typeofactivityid: Int?,
    var position: Int?,
    val gruop: Int?,
    var dependence: String?,
)
{
    constructor() : this(
        id = null,
        name = "",
        status = null,
        start_date = null,
        scope = null,
        description = null,
        parent = null,
        userCount = null,
        generation =1,
        typeofactivityid = null,
        position = null,
        gruop = null,
        dependence = null,
    )
}
