package com.example.db.comments

import kotlinx.serialization.Serializable

@Serializable
class CommentDTO(
    val id:Int?,
    val user: Int?,
    val comments: String) {
}