package com.example.plugins

import com.example.db.Task.TaskModel
import mu.KotlinLogging
import java.io.File

private val logger = KotlinLogging.logger {}
fun createMedia(name:String)
{
    var photoPath = "src\\main\\resources\\media\\${name}\\photo\\"


    val photo = File(photoPath)
    if (!photo.exists()) {
        if (!photo.mkdirs()) {
            logger.info { "Фаил photo уже существует создан" }
        }
        else
        {
            logger.info { "Фаил photo создан" }
        }
    }

    var filePath = "src\\main\\resources\\media\\${name}\\file\\"


    val file = File(filePath)
    if (!file.exists()) {
        if (!file.mkdirs()) {
            logger.info { "Фаил file уже существует создан" }
        }
        else
        {
            logger.info { "Фаил file создан" }
        }
    }

    var videoPath = "src\\main\\resources\\media\\${name}\\video\\"


    val video = File(videoPath)
    if (!video.exists()) {
        if (!video.mkdirs()) {
            logger.info { "Фаил video уже существует создан" }
        }
        else
        {
            logger.info { "Фаил video создан" }
        }
    }
}
