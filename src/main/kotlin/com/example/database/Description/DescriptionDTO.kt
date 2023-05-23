package com.example.db.Description

import kotlinx.serialization.Serializable

@Serializable
class DescriptionDTO(
    val id:Int?,
    val content:String,
    val file_resources: ByteArray?,
    val photo_resources: ByteArray?,
    val video_resources: ByteArray?
) {
}