package com.example.database.Person

import kotlinx.serialization.Serializable

@Serializable
class PersonDTO (
    val id:Int?,
    val surname: String,
    val name:String,
    val patronymic: String?,
    val type_of_activity: Int? = null
    )
