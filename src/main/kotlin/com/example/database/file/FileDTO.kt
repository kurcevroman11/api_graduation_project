package com.example.database.file

import kotlinx.serialization.Serializable

@Serializable
class FileDTO (
    val id : Int?,
    val orig_filename : String?,
    val taskId: Int?,
    val type : String?

){
}