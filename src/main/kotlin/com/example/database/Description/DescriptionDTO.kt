package com.example.db.Description

import kotlinx.serialization.Serializable

@Serializable
class DescriptionDTO(
    val id:Int?,
    val content:String,
    val file_resources: String?,
    val photo_resources: String?,
    val video_resources: String?
) {
}