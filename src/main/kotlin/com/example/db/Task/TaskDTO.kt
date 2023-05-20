package com.example.db.Task

import kotlinx.serialization.Serializable


@Serializable
class TaskDTO(
    var id: Int?,
    val name: String,
    val status: Int?,
    val start_date:String?,
    val scope:String?,
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
