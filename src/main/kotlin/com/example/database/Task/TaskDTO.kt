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
    val scope: Int?,
    var description: Int?,
    var parent: Int?,
    val comments: Int?)
{
    constructor() : this(
        id = null,
        name = "",
        status = null,
        start_date = null,
        scope = null,
        description = null,
        parent = null,
        comments = null
    )
}
