package com.example.db.Task

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.joda.time.DateTime
import java.time.LocalTime


@Serializable
class TaskDTO(
    var id: Int?,
    val name: String,
    val status: Int?,
    @Contextual
    val start_date: DateTime?,
    @Contextual
    val scope: DateTime?,
    val description: Int?,
    val parent: Int?,
    val generathon: Int?,
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
        generathon = null,
        comments = null
    )
}
